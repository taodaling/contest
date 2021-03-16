package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.MillerRabin;
import template.math.ModPrimeInverseNumber;
import template.math.Power;

public class P3599KoishiLovesConstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int t = in.ri();
        for (int i = 0; i < t; i++) {
            int[] ans;
            if (x == 1) {
                ans = solveSum(in, out);
            } else {
                ans = solveProd(in, out);
            }
            if (ans == null) {
                out.println(0);
            } else {
                out.append(2).append(' ');
                for (int a : ans) {
                    out.append(a == 0 ? ans.length : a).append(' ');
                }
                out.println();
            }
        }
    }

    public int[] solveProd(FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 1) {
            return new int[]{0};
        }
        if (n == 4) {
            return new int[]{1, 3, 2, 0};
        }
        if (!MillerRabin.mr(n, 10)) {
            return null;
        }
        InverseNumber inv = new ModPrimeInverseNumber(n - 1, n);
        int[] seq = new int[n];
        seq[0] = 1;
        for (int i = 1; i < n; i++) {
            //i + 1 / i
            seq[i] = (int) ((i + 1) * (long) inv.inverse(i) % n);
        }
        return seq;
    }

    public int[] solveSum(FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 1) {
            return new int[]{0};
        }
        if (n % 2 == 1) {
            return null;
        }
        int[] seq = new int[n];
        for (int i = 0; i < n; i += 2) {
            seq[i] = (n - i) % n;
        }
        for (int i = 1; i < n; i += 2) {
            seq[i] = i;
        }
        return seq;
    }
}
