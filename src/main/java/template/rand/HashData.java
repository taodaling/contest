package template.rand;

import template.math.DigitUtils;
import template.utils.Pair;

import java.math.BigInteger;

public class HashData {
    public int mod;
    public int[] inv;
    public int[] pow;
    public int x;

    public HashData(int n, int x, int p) {
        this.mod = p;
        this.x = x;
        inv = new int[n + 10];
        pow = new int[n + 10];
        inv[0] = 1;
        pow[0] = 1;
        int invX = BigInteger.valueOf(x).modInverse(BigInteger.valueOf(p)).intValue();
        for (int i = 1; i < n + 10; i++) {
            inv[i] = (int) ((long) inv[i - 1] * invX % mod);
            pow[i] = (int) ((long) pow[i - 1] * x % mod);
        }
    }

    public HashData(int n, int x) {
        this(n, x, (int) 1e9 + 7);
    }

    public HashData(int n) {
        this(n, RandomWrapper.INSTANCE.nextInt(3, (int) 1e9 + 6), (int) 1e9 + 7);
    }

    public int hash(int x) {
        return DigitUtils.mod(x, mod);
    }

    public int hash(long x) {
        long high = x >>> 32;
        long low = x & ((1L << 32) - 1);
        return (int) ((high * this.x + low) % mod);
    }

    public int hash(double x) {
        return hash(Double.doubleToRawLongBits(x));
    }

    public static HashData[] doubleHashData(int n) {
        Pair<Integer, Integer> pairs = HashSeed.getSeed2();
        return new HashData[]{new HashData(n, pairs.a), new HashData(n, pairs.b)};
    }
}
