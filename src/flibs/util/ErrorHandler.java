package flibs.util;

public class ErrorHandler {

    protected ErrorHandler() {}

    public static void fatal(String message, Exception e) {
        System.err.println("Fatal: " + message);
        if (e != null) e.printStackTrace();
        System.exit(-1);
    }

    public static void soft(String message, Exception e) {
        System.err.println(message);
        if (e != null) e.printStackTrace();
    }
}
