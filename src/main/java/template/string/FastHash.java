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
