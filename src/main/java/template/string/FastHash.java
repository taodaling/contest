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
        for (int i = 0; i < seq.length(); i++) {
            int x = seq.get(i);
            res1 = (res1 * x1 + x) % mod;
            res2 = (res2 * x2 + x) % mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int... seq) {
        long res1 = 0;
        long res2 = 0;
        for (int x : seq) {
            res1 = (res1 * x1 + x) % mod;
            res2 = (res2 * x2 + x) % mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b) {
        long res1 = ((long) a * x1 + b) % mod;
        long res2 = ((long) b * x2 + b) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c) {
        long res1 = (((long) a * x1 + b) % mod * x1 + c) % mod;
        long res2 = (((long) a * x2 + b) % mod * x2 + c) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

    public long hash(int a, int b, int c, int d) {
        long res1 = ((((long) a * x1 + b) % mod * x1 + c) * x1 % mod + d) % mod;
        long res2 = ((((long) a * x2 + b) % mod * x2 + c) * x2 % mod + d) % mod;
        if (res1 < 0) {
            res1 += mod;
        }
        if (res2 < 0) {
            res2 += mod;
        }
        return DigitUtils.asLong(res1, res2);
    }

}
