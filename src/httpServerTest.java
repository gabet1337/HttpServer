import java.io.*;

import org.junit.*;
import static org.junit.Assert.*;

public class httpServerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private HttpServer hs;
    
    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUp() {
        System.setOut(System.out);
    }
    
    @Test
    public void shouldPrintHelloWorld() {
        System.out.print("Hello World\n");
        assertEquals("Hello World\n", outContent.toString());
    }
    
    @Test
    public void serverShouldPrintGreetings() {
        hs = new WebServer(8070,"");
        hs.start();
        assertEquals("WebServer accepting connections on port 8070\n", outContent.toString());
    }
    
    //@Test()
    //public void shouldNotBeAbleToStartTwoIdenticalServers() {
        //hs = new WebServer(8070,"");
        //hs.start();
        //assertEquals("WebServer accepting connections on port 8070\n", outContent.toString());
        //WebServer ws = new WebServer(8070,"");
        //ws.start();
        //assertTrue("Should not be able to start server", outContent.toString().startsWith("Could not start webserver: "));
    //}
    
}
