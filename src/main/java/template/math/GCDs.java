package template.math;

public class GCDs {
    private GCDs(){}
    public static long gcd(long a, long b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private static long gcd0(long a, long b) {
        return b == 0 ? a : gcd0(b, a % b);
    }

    public static int gcd(int a, int b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private static int gcd0(int a, int b) {
        return b == 0 ? a : gcd0(b, a % b);
    }
}
