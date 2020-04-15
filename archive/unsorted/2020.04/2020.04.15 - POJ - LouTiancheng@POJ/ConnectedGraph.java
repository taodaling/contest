package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class ConnectedGraph {
    BigInteger[][] comp = new BigInteger[51][51];
    BigInteger[] pow2 = new BigInteger[2000];
    BigInteger[] f = new BigInteger[51];

    public BigInteger pow2(int i) {
        if (pow2[i] == null) {
            if (i == 0) {
                return pow2[i] = BigInteger.ONE;
            }
            pow2[i] = pow2(i - 1).multiply(BigInteger.valueOf(2));
        }
        return pow2[i];
    }

    public BigInteger comp(int n, int m) {
        if (n < m) {
            return BigInteger.ZERO;
        }
        if (comp[n][m] == null) {
            if (m == 0) {
                return comp[n][m] = BigInteger.ONE;
            }
            comp[n][m] = comp(n - 1, m).add(comp(n - 1, m - 1));
        }
        return comp[n][m];
    }

    int pick2(int n) {
        return n * (n - 1) / 2;
    }

    public BigInteger f(int n) {
        if (f[n] == null) {
            f[n] = pow2(pick2(n));
            for (int i = 1; i <= n - 1; i++) {
                BigInteger sub = comp(n - 1, i - 1).multiply(f(i)).multiply(pow2(pick2(n - i)));
                f[n] = f[n].subtract(sub);
            }
        }
        return f[n];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n == 0) {
            throw new UnknownError();
        }
        out.println(f(n));
    }
}
