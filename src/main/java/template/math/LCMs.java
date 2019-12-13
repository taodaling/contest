package template.math;

public class LCMs {
    private LCMs(){}
    public static long lcm(long x, long y) {
        return x / GCDs.gcd(x, y) * y;
    }
}
