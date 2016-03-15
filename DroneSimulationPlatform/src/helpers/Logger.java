package helpers;

import java.io.PrintWriter;


public class Logger {
    private Boolean verbose;
    private String fileName;
    private PrintWriter writer;

    public Logger(String inFileName, Boolean inVerbose) {
        fileName = inFileName;
        verbose = inVerbose;

        try {
            writer = new PrintWriter(fileName, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
        writer.println(message);
    }


    public void close(){
        writer.close();
    }
}
