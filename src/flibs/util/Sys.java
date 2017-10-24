package flibs.util;

public class Sys {

    public enum OS {
        WINDOWS,
        LINUX,
        MAC
    }

    public static Sys.OS getOS() {
        return OS.LINUX;
    }

    public static int geyOSBytes() {
        return Integer.parseInt( System.getProperty("sun.arch.data.model") );
    }
}
