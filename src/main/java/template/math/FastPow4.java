package template.math;

public class FastPow4 {
    static int mask = (1 << 8) - 1;
    int mod;
    int phi;
    int[] x0 = new int[mask + 1];
    int[] x1 = new int[mask + 1];
    int[] x2 = new int[mask + 1];
    int[] x3 = new int[mask + 1];
    private int xphi;
    public FastPow4(int x, int mod) {
        this.mod = mod;
        phi = CachedEulerFunction.get(mod);
        xphi = DigitUtils.modPow(x, phi, mod);

        x0[0] = x1[0] = x2[0] = x3[0] = 1;
        long base = x;
        for (int i = 1; i <= mask; i++) {
            x0[i] = (int) (x0[i - 1] * base % mod);
        }
        base = x0[mask] * base % mod;
        for (int i = 1; i <= mask; i++) {
            x1[i] = (int) (x1[i - 1] * base % mod);
        }
        base = x1[mask] * base % mod;
        for (int i = 1; i <= mask; i++) {
            x2[i] = (int) (x2[i - 1] * base % mod);
        }
        base = x2[mask] * base % mod;
        for (int i = 1; i <= mask; i++) {
            x3[i] = (int) (x3[i - 1] * base % mod);
        }
    }

    public int pow(int n) {
        long ans = x0[n & mask] % mod;
        n >>>= 8;
        ans = ans * x1[n & mask] % mod;
        n >>>= 8;
        ans = ans * x2[n & mask] % mod;
        n >>>= 8;
        ans = ans * x3[n & mask] % mod;
        return (int) ans;
    }

    public int pow(long n) {
        if (n < Integer.MAX_VALUE) {
            return pow((int) n);
        }
        return (int) ((long) pow((int) (n % phi)) * xphi % mod);
    }

    /**
     * assume mod is a prime
     */
    public int inverse(long n) {
        assert phi == mod - 1;
        n %= phi;
        return pow((mod - 2) * n);
    }
}
