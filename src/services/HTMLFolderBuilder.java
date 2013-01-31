package services;

import java.io.File;

public class HTMLFolderBuilder implements FolderBuilder {

    private String result = new String();
    private File index;
    
    public HTMLFolderBuilder(File index) {
        this.index = index;
    }
    
    
    public void parseFile(File file) {
        result += "<tr><td><a href=\"" + file.getName() + "\">" + file.getName() + "</a></td><td align=\"right\">" + file.length() + "</td></tr>";
    }

    public void parseFolder(File file) {
        result += "<tr><td><a href=\"" + file.getName() + "\">" + file.getName() + "</a></td><td align=\"right\">  - </td></tr>";

    }
    
    private String setupHTMLResult() {
        String res ="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                    "<html>" +
                    "<head>" +
                    "<title> Index of " + index.getName() + "</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1>Index of " + index.getName() + "</h1>" +
                    "<table>" +
                    "<tr> <th>Name</th><th>Size</th></tr><tr><th colspan=\"5\"><hr></th></tr>";
        if (index.getParent() != null) {
            return res + "<tr><td><a href=\"" + index.getParent() + "\">" + index.getParent() + "</a></td><td align=\"right\">  - </td></tr>";
        } else {
            return res;
        }
        
    }
    
    private String endHTMLResult() {
        return      "</table>" +
                    "</body></html>";
    }
    
    public String getResult() {
        return setupHTMLResult() + result + endHTMLResult();
    }

}
