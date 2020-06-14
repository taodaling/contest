package template.math;

public class LongExtGCDObject {
    private long[] xy = new long[2];

    public long extgcd(long a, long b) {
        return ExtGCD.extGCD(a, b, xy);
    }

    public long getX() {
        return xy[0];
    }

    public long getY() {
        return xy[1];
    }
}
