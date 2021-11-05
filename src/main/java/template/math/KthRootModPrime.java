package template.math;

import java.util.HashMap;

/**
 * https://judge.yosupo.jp/submission/7053
 */
public class KthRootModPrime {
    /**
     * a^{1/k} \pmod p
     * @param a
     * @param k
     * @param p
     * @return
     */
    public static long kth_root(long a, long k, long p) {
        if (k == 0) return a == 1 ? 1 : -1;
        if (k > 0 && a == 0) return 0;
        long g = gcd(k, p - 1);
        if (pow(a, (p - 1) / g, p) != 1)
            return -1;
        a = pow(a, inv(k / g, (p - 1) / g), p);
        for (long div = 2; div * div <= g; ++div) {
            int sz = 0;
            while (g % div == 0) {
                g /= div;
                ++sz;
            }
            if (sz > 0) {
                long b = peth_root(a, div, sz, p);
                a = b;
            }
        }
        if (g > 1)
            a = peth_root(a, g, 1, p);
        return a;
    }

    private static long peth_root(long a, long p, int e, long mod) {
        long q = mod - 1;
        int s = 0;
        while (q % p == 0) {
            q /= p;
            ++s;
        }
        long pe = pow(p, e, mod);
        long ans = pow(a, ((pe - 1) * inv(q, pe) % pe * q + 1) / pe, mod);
        long c = 2;
        while (pow(c, (mod - 1) / p, mod) == 1)
            ++c;
        c = pow(c, q, mod);
        HashMap<Long, Integer> map = new HashMap<>();
        long add = 1;
        int v = (int) Math.sqrt(p) + 1;
        long mul = pow(c, v * pow(p, s - 1, mod - 1), mod);
        for (int i = 0; i <= v; ++i) {
            map.put(add, i);
            add = add * mul % mod;
        }
        mul = inv(pow(c, pow(p, s - 1, mod - 1), mod), mod);
        out:
        for (int i = e; i < s; ++i) {
            long err = inv(pow(ans, pe, mod), mod) * a % mod;
            long target = pow(err, pow(p, s - 1 - i, mod - 1), mod);
            for (int j = 0; j <= v; ++j) {
                if (map.containsKey(target % mod)) {
                    int x = map.get(target % mod);
                    ans = ans * pow(c, (j + v * x) * pow(p, i - e, mod - 1) % (mod - 1), mod) % mod;
                    continue out;
                }
                target = target * mul % mod;
            }
            throw new AssertionError();
        }
        return ans;
    }

    private static int cnt(long a, long base, long p) {
        int ret = 0;
        while (a != 1) {
            a = pow(a, base, p);
            ++ret;
        }
        return ret;
    }

    private static long inv(long a, long p) {
        a %= p;
        long u = 1, v = 0;
        long b = p;
        while (b > 0) {
            long q = a / b;
            a %= b;
            u -= v * q % p;
            u %= p;
            {
                u ^= v;
                v ^= u;
                u ^= v;
                a ^= b;
                b ^= a;
                a ^= b;
            }
        }
        return u < 0 ? u + p : u;
    }

    private static long pow(long a, long n, long p) {
        n %= p - 1;
        long r = 1;
        for (; n > 0; n >>= 1, a = a * a % p)
            if (n % 2 == 1)
                r = r * a % p;
        return r;
    }

    private static long gcd(long a, long b) {
        return a == 0 ? b : gcd(b % a, a);
    }
}
