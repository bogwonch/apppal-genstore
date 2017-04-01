package genstore;

public final class Log {
    private final static String ERROR = "\033[0;31m[ERROR]\033[0m ";
    private final static String WARN = "\033[0;32m[WARNING]\033[0m ";
    private final static String INFO = "\033[0;34m[INFO]\033[0m ";
    private final static String DEBUG = "\033[0;35m[DEBUG]\033[0m ";

    public static boolean debug = false;

    private static void print(String kind, String message) {
        System.err.println(kind + message);
    }

    public static void error(String message) {
        print(ERROR, message);
        System.exit(-1);
    }

    public static void warn(String message) {
        print(WARN, message);
    }

    public static void info(String message) {
        print(INFO, message);
    }

    public static void debug(String message) {
        if (debug) {
            print(DEBUG, message);
        }
    }
}
