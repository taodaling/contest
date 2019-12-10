package template.math;

import template.datastructure.ByteList;
import template.math.Modular;

import java.util.BitSet;

/**
 * Power operations
 */
public class Power {
    public Modular getModular() {
        return modular;
    }

    final Modular modular;

    public Power(Modular modular) {
        this.modular = modular;
    }

    public int pow(int x, long n) {
        if (n == 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n >> 1);
        r = modular.valueOf(r * r);
        if ((n & 1) == 1) {
            r = modular.valueOf(r * x);
        }
        return (int) r;
    }

    public int pow(int x, int n) {
        if (n == 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n >> 1);
        r = modular.valueOf(r * r);
        if ((n & 1) == 1) {
            r = modular.valueOf(r * x);
        }
        return (int) r;
    }

    public int pow(int x, BitSet n) {
        return pow(x, n, n.size() - 1);
    }

    private int pow(int x, BitSet n, int i) {
        if (i < 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n, i - 1);
        r = modular.valueOf(r * r);
        if (n.get(i)) {
            r = modular.valueOf(r * x);
        }
        return (int) r;
    }

    public int inverse(int x) {
        return pow(x, modular.m - 2);
    }

    public int pow2(int x) {
        return x * x;
    }

    public long pow2(long x) {
        return x * x;
    }

    public double pow2(double x) {
        return x * x;
    }
}
