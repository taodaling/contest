package template.string;

import template.math.DigitUtils;
import template.rand.HashSeed;
import template.utils.Pair;

public class FastHash {
    long x1;
    long x2;
    int mod;

    public FastHash(int mod, int x1, int x2) {
        this.mod = mod;
        this.x1 = x1;
        this.x2 = x2;
    }

    public FastHash() {
        this.mod = (int) 1e9 + 7;
        Pair<Integer, Integer> seeds = HashSeed.getSeed2();
        x1 = seeds.a;
        x2 = seeds.b;
    }

    public long hash(IntSequence seq) {
        long res1 = 0;
        long res2 = 0;
        for (int i = seq.length() - 1; i >= 0; i--) {
            int x = seq.get(i);
            res1 = (res1 * x1 + x) % mod;
            res2 = (res2 * x2 + x) % mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int... seq) {
        return hash(seq, seq.length);
    }

    public long hash(long a, long b) {
        return hash(DigitUtils.highBit(a), DigitUtils.lowBit(a), DigitUtils.highBit(b), DigitUtils.lowBit(b));
    }

    public long hash(long a, long b, long c) {
        return hash(DigitUtils.highBit(a), DigitUtils.lowBit(a), DigitUtils.highBit(b), DigitUtils.lowBit(b),
                DigitUtils.highBit(c), DigitUtils.lowBit(c));
    }

    public long hash(long a, long b, long c, long d) {
        return hash(DigitUtils.highBit(a), DigitUtils.lowBit(a), DigitUtils.highBit(b), DigitUtils.lowBit(b),
                DigitUtils.highBit(c), DigitUtils.lowBit(c), DigitUtils.highBit(d), DigitUtils.lowBit(d));
    }

    public long hash(int a, int b) {
        long res1 = ((long) b * x1 + a) % mod;
        long res2 = ((long) b * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c) {
        long res1 = ((c * x1 + b) % mod * x1 + a) % mod;
        long res2 = ((c * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d) {
        long res1 = (((d * x1 + c) % mod * x1 + b) % mod * x1 + a) % mod;
        long res2 = (((d * x2 + c) % mod * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d, int e) {
        long res1 = ((((e * x1 + d) % mod * x1 + c) % mod * x1 + b) % mod * x1 + a) % mod;
        long res2 = ((((e * x2 + d) % mod * x2 + c) % mod * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d, int e, int f) {
        long res1 = (((((f * x1 + e) % mod * x1 + d) % mod * x1 + c) % mod * x1 + b) % mod * x1 + a) % mod;
        long res2 = (((((f * x2 + e) % mod * x2 + d) % mod * x2 + c) % mod * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d, int e, int f, int g) {
        long res1 = ((((((g * x1 + f) % mod * x1 + e) % mod * x1 + d) % mod * x1 + c) % mod * x1 + b) % mod * x1 + a) % mod;
        long res2 = ((((((g * x2 + f) % mod * x2 + e) % mod * x2 + d) % mod * x2 + c) % mod * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d, int e, int f, int g, int h) {
        long res1 = (((((((h * x1 + g) % mod * x1 + f) % mod * x1 + e) % mod * x1 + d) % mod * x1 + c) % mod * x1 + b) % mod * x1 + a) % mod;
        long res2 = (((((((h * x2 + g) % mod * x2 + f) % mod * x2 + e) % mod * x2 + d) % mod * x2 + c) % mod * x2 + b) % mod * x2 + a) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int[] data, int n) {
        long res1 = 0;
        long res2 = 0;
        for (int i = n - 1; i >= 0; i--) {
            res1 = (res1 * x1 + data[i]) % mod;
            res2 = (res2 * x2 + data[i]) % mod;
        }
        return DigitUtils.asLong(res1, res2);
    }
}
