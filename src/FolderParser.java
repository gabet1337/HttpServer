import java.io.*;

public class FolderParser {

    private FolderBuilder builder;
    private File folder;

    public FolderParser(FolderBuilder builder, String path) {
        this.builder = builder;
        this.folder = new File(path);
    }
    
    public String parse() {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                builder.parseFolder(f);
            } else if (f.isFile()) {
                builder.parseFile(f);
            } else {
                //Do nothing if it isn't a file or folder
            }
        }
        return builder.getResult();
    }
}
