package template.math;

import java.util.BitSet;

/**
 * Power operations
 */
public class Power implements InverseNumber {
    public Modular getModular() {
        return modular;
    }

    static IntExtGCDObject extGCD = new IntExtGCDObject();

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

    public int inverseByFermat(int x) {
        return pow(x, modular.m - 2);
    }

    @Override
    public int inverse(int x) {
        int ans =  inverseExtGCD(x);
//        if(modular.mul(ans, x) != 1){
//            throw new IllegalStateException();
//        }
        return ans;
    }

    public int inverseExtGCD(int x) {
        if (extGCD.extgcd(x, modular.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        return modular.valueOf(extGCD.getX());
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
