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
    int modVal;

    public Power(Modular modular) {
        this.modular = modular;
        this.modVal = modular.getMod();
    }

    public int pow(int x, long n) {
        if (n == 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n >> 1);
        r = r * r % modVal;
        if ((n & 1) == 1) {
            r = r * x % modVal;
        }
        return (int) r;
    }

    public int pow(int x, int n) {
        if (n == 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n >> 1);
        r = r * r % modVal;
        if ((n & 1) == 1) {
            r = r * x % modVal;
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
        r = r * r % modVal;
        if (n.get(i)) {
            r = r * x % modVal;
        }
        return (int) r;
    }

    public int inverseByFermat(int x) {
        return pow(x, modVal - 2);
    }

    @Override
    public int inverse(int x) {
        int ans = inverseExtGCD(x);
//        if(modular.mul(ans, x) != 1){
//            throw new IllegalStateException();
//        }
        return ans;
    }

    public int inverseExtGCD(int x) {
        if (extGCD.extgcd(x, modVal) != 1) {
            throw new IllegalArgumentException();
        }
        return modular.valueOf(extGCD.getX());
    }

}
