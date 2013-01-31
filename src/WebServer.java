import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer implements HttpServer {

    private int port;
    private String home;
    private Socket connection;
    private BufferedReader input;
    private OutputStream output;
    private PrintStream print;

    public WebServer(int port, String home) {
        this.port = port;
        this.home = home;
    }

    public static void main(String[] args) {

        if (args.length != 1 && args.length != 2) {
            System.out.println("Usage: java WebServer <port> <wwwhome>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);
        String home = System.getProperty("user.dir") + "/FilesToBeServed/";

        if (args.length == 2) {
            home = args[1];
        }

        WebServer ws = new WebServer(port, home); 
        ws.start();

    }

    @Override
    public void start() {

        ServerSocket ss = null; 
        try {
            ss = new ServerSocket(port); 
        } catch (IOException e) {
            System.err.println("Could not start webserver: "+e);
            System.exit(-1);
        }

        System.out.print("WebServer accepting connections on port " + port + "\n");

        while (true) {
            try {
                connection = ss.accept();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                output = new BufferedOutputStream(connection.getOutputStream());
                print = new PrintStream(output);

                String request = input.readLine();
                connection.shutdownInput(); // ignore the rest
                log(connection, request);

                if (request != null)
                    processRequest(request);

                print.flush();

            } catch (IOException e) { 
                System.err.println(e); 
            }
            try {
                if (connection!=null) 
                    connection.close(); 
            } catch (IOException e) { 
                System.err.println(e); 
            }
        }
    }


    void processRequest(String request) throws IOException {           
        
        if (!request.startsWith("GET") || (!request.endsWith("HTTP/1.0") && !request.endsWith("HTTP/1.1")) || request.charAt(4)!='/')
            return;

        String req = request.substring(4, request.length()-9).trim();

        if (req.indexOf("/.")!=-1 || req.endsWith("~")) {
            errorReport(print, connection, "403", "Forbidden", "No permission to access the requested URL.");
            return;
        }

        if (req.equals("/"))
            req = req + "/index.html";
        
        if (req.endsWith("/")) {
            print.print("301 HTTP/1.0");
            print.print("Location: "+ connection.getInetAddress().getHostAddress()+
                ":"+connection.getPort()+ req.substring(0, req.length()-1));
            return;
        }
        
        
        String path = home + "/" + req;
        File f = new File(path);
        
        if (f.isDirectory()) {
            FolderParser fp = new FolderParser(new HTMLFolderBuilder(f, req), path);
            send(print, connection, "200", fp.parse());
            return;
        }
        
        if (req.startsWith("/shell-script.sh")) {
            try {
                send(print, connection, "200", ShellScriptExecutor.execute(req.replace("/shell-script.sh?INPUT=","").replace("&sendtext=","")));
            } catch (IOException e) {
                errorReport(print,connection,"500","Internal server error","Houston we have a problem.");
            }
            return;
        }
        
        try { 
            InputStream file = new FileInputStream(f);
            String contenttype = URLConnection.guessContentTypeFromName(path);
            print.print("HTTP/1.0 200 OK\r\n");

            if (contenttype!=null)
                print.print("Content-Type: "+contenttype+"\r\n");

            print.print("Date: "+new Date()+"\r\n" + "Server: dDist webServer 1.0\r\n\r\n");
            sendFile(file, output); 
            log(connection, "200 OK");
        } catch (FileNotFoundException e) { 
            errorReport(print, connection, "404", "Not Found", "The requested URL was not found on this server.");
        }
        
    }

    void log(Socket con, String msg) {
        System.out.println(new Date()+" ["+
                con.getInetAddress().getHostAddress()+
                ":"+con.getPort()+"] "+msg);
    }

    @Override
    public void close() {
        System.exit(0);
    }


    void send(PrintStream pout, Socket con,
            String code, String msg) {
        pout.print(msg);
    }
    
    void errorReport(PrintStream pout, Socket con,
            String code, String title, String msg) {
        pout.print(
                "HTTP/1.0 "+code+" "+title+"\r\n"+
                        "\r\n"+
                        "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n"+
                        "<HTML><HEAD><TITLE>"+code+" "+title+"</TITLE>\r\n"+
                        "</HEAD><BODY>\r\n"+
                        "<H1>"+title+"</H1>\r\n"+msg+"<P>\r\n"+
                        "<HR><ADDRESS>dDist webserver 1.0 at "+
                        con.getLocalAddress().getHostName()+
                        " Port "+con.getLocalPort()+"</ADDRESS>\r\n"+
                "</BODY></HTML>\r\n");
        log(con, code+" "+title);
    }

    void sendFile(InputStream file, OutputStream out) 
            throws IOException {
        byte[] buffer = new byte[1000];
        while (file.available()>0) 
            out.write(buffer, 0, file.read(buffer));
    }
}
