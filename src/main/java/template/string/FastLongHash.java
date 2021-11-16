package template.string;

import template.math.ILongModular;
import template.math.LongModular2305843009213693951;
import template.rand.RandomWrapper;

public class FastLongHash {
    static ILongModular modular = LongModular2305843009213693951.getInstance();
    static long mod = modular.getMod();
    long x = RandomWrapper.INSTANCE.nextLong(1, mod - 1);

    public long hash(long x0) {
        return x0;
    }

    public long hash(long x0, long x1) {
        long ans = modular.mul(x1, x) + x0;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }


    public long hash(long x0, long x1, long x2) {
        long ans = modular.mul(x2, x) + x1;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x0;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }

    public long hash(long x0, long x1, long x2, long x3) {
        long ans = modular.mul(x3, x) + x2;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x1;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x0;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }

    public long hash(long x0, long x1, long x2, long x3, long x4) {

        long ans = modular.mul(x4, x) + x3;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x2;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x1;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x0;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }

    public long hash(long x0, long x1, long x2, long x3, long x4, long x5) {
        long ans = modular.mul(x5, x) + x4;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x3;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x2;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x1;
        if (ans >= mod) {
            ans -= mod;
        }
        ans = modular.mul(ans, x) + x0;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }
}
