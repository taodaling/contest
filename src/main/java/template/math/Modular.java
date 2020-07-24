package template.math;

/**
 * Mod operations
 */
public class Modular {
    static MillerRabin mr = new MillerRabin();

    int m;
    private boolean isPrime;

    public boolean isPrime() {
        return isPrime;
    }

    public int getMod() {
        return m;
    }

    public Modular(int m) {
        this.m = m;
        isPrime = mr.mr(m, 10);
    }

    public Modular(long m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public Modular(double m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public int valueOf(int x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return x;
    }

    public int valueOf(long x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return (int) x;
    }

    public int mul(int x, int y) {
        return valueOf((long) x * y);
    }

    public int mul(long x, long y) {
        return valueOf(x * y);
    }

    public int plus(int x, int y) {
        return valueOf(x + y);
    }

    public int plus(long x, long y) {
        return valueOf(x + y);
    }

    public int subtract(int x, int y) {
        return valueOf(x - y);
    }

    public int subtract(long x, long y) {
        return valueOf(x - y);
    }

    public Modular getModularForPowerComputation() {
        return new Modular(m - 1);
    }

    @Override
    public String toString() {
        return "mod " + m;
    }
}
