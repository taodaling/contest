package template.binary;

/**
 * Log operations
 */
public class Log2 {
    public static int ceilLog(int x) {
        return 32 - Integer.numberOfLeadingZeros(x - 1);
    }

    public static int floorLog(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    public static int ceilLog(long x) {
        return 64 - Long.numberOfLeadingZeros(x - 1);
    }

    public static int floorLog(long x) {
        return 63 - Long.numberOfLeadingZeros(x);
    }
}
