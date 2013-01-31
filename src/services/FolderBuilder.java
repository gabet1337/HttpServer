package services;

import java.io.*;

public interface FolderBuilder {

    /*
     * Parses a file
     */
    public void parseFile(File file);

    /*
     * Parses a folder
     */
    public void parseFolder(File file);

    /*
     * Returns the result as a string
     */
    public String getResult();

}
