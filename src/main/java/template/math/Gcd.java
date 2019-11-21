package template.math;

public class Gcd {
    public long gcd(long a, long b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private long gcd0(long a, long b) {
        return b == 0 ? a : gcd0(b, a % b);
    }

    public int gcd(int a, int b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private int gcd0(int a, int b) {
        return b == 0 ? a : gcd0(b, a % b);
    }
}
