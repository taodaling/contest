package template.math;

/**
 * Log operations
 */
public class Log2 {
    public int ceilLog(int x) {
        return 32 - Integer.numberOfLeadingZeros(x - 1);
    }

    public int floorLog(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    public int ceilLog(long x) {
        return 64 - Long.numberOfLeadingZeros(x - 1);
    }

    public int floorLog(long x) {
        return 63 - Long.numberOfLeadingZeros(x);
    }
}
