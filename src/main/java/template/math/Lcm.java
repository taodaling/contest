package template.math;

public class Lcm {
    private Gcd gcd = new Gcd();

    public long lcm(long x, long y) {
        return x / gcd.gcd(x, y) * y;
    }
}
