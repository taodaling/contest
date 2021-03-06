package template.math;

import java.util.BitSet;

/**
 * Power operations
 */
public class Power implements InverseNumber {
    public int getMod() {
        return mod;
    }

    int mod;

    public Power(int mod) {
        this.mod = mod;
    }

    public int pow(int x, long n) {
        if (n == 0) {
            return 1 % mod;
        }
        long r = pow(x, n >> 1);
        r = r * r % mod;
        if ((n & 1) == 1) {
            r = r * x % mod;
        }
        return (int) r;
    }

    public int pow(int x, int n) {
        if (n == 0) {
            return 1 % mod;
        }
        long r = pow(x, n >> 1);
        r = r * r % mod;
        if ((n & 1) == 1) {
            r = r * x % mod;
        }
        return (int) r;
    }

    public int pow(int x, BitSet n) {
        return pow(x, n, n.size() - 1);
    }

    private int pow(int x, BitSet n, int i) {
        if (i < 0) {
            return 1 % mod;
        }
        long r = pow(x, n, i - 1);
        r = r * r % mod;
        if (n.get(i)) {
            r = r * x % mod;
        }
        return (int) r;
    }

    public int inverseByFermat(int x) {
        return pow(x, mod - 2);
    }

    public int inversePower(int x, long n) {
        n = DigitUtils.mod(-n, mod - 1);
        return pow(x, n);
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
        return (int)DigitUtils.modInverse(x, mod);
    }

}
