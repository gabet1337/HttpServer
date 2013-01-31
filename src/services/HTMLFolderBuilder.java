package services;
import java.io.*;

public class HTMLFolderBuilder implements FolderBuilder {

    private String result = new String();
    private File index;
    private String home;
    
    public HTMLFolderBuilder(File index, String home) {
        this.index = index;
        this.home = home;
    }
    
    
    public void parseFile(File file) {
        result += "<tr><td><a href=\"" + home + "/" + file.getName() + "\">" + file.getName() + "</a></td><td align=\"right\">" + file.length() + "</td></tr>\n";
    }

    public void parseFolder(File file) {
        result += "<tr><td><a href=\"" +  home + "/" + file.getName() + "\">" + file.getName() + "</a></td><td align=\"right\">  - </td></tr>\n";

    }
    
    private String setupHTMLResult() {
        String res ="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title> Index of " + index.getName() + "</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>Index of " + index.getName() + "</h1>\n" +
                    "<table>\n" +
                    "<tr> <th>Name</th><th>Size</th></tr><tr><th colspan=\"5\"><hr></th></tr>\n";
        if (index.getParent() != null) {
        	String link = home.substring(0,home.lastIndexOf("/"));
        	if (link.equals(""))
        		link = "/";
            return res + "<tr><td><a href=\"" + link + "\">" + ".." + "</a></td><td align=\"right\">  - </td></tr>\n";
        } else {
            return res;
        }
        
    }
    
    private String endHTMLResult() {
        return      "</table>\n" +
                    "</body></html>";
    }
    
    public String getResult() {
        return setupHTMLResult() + result + endHTMLResult();
    }

}