package template.problem;


import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class CountSubsetSum {
    public static void main(String[] args) {
        new CountSubsetSum(new int[]{1, 1, 2, 2 ,3}, 2, 998244353, new IntPolyNTT(998244353));
    }

    int[] ans;
    int mod;

    public CountSubsetSum(int[] s, int n, int mod, IntPoly poly) {
        this(s, n, mod, poly, i -> 1);
    }

    /**
     * s[i] >= 1 for all i should satisfied. Run in O(|s|+n\log_2n).
     */
    /**
     * another interpretation:
ra
     */
    public CountSubsetSum(int[] s, int n, int mod, IntPoly poly, IntToIntFunction c) {
        this.mod = mod;
        int[] cnt = PrimitiveBuffers.allocIntPow2(n + 1);
        for (int x : s) {
            if (x <= n) {
                cnt[x]++;
            }
        }
        int[] log = PrimitiveBuffers.allocIntPow2(n + 1);
        int[] invBuf = PrimitiveBuffers.allocIntPow2(n + 1);
        InverseNumber inv = new ModPrimeInverseNumber(invBuf, n, mod);
        for (int i = 1; i <= n; i++) {
            int ij;
            long prod = 1;
            int x = c.apply(i);
            for (int j = 1; (ij = i * j) <= n; j++) {
                prod = prod * x % mod;
                long invJ = inv.inverse(j);
                long contrib = invJ * cnt[i] % mod * prod;
                if ((j & 1) == 0) {
                    contrib = -contrib;
                }
                log[ij] = (int) ((log[ij] + contrib) % mod);
            }
        }
        for (int i = 0; i <= n; i++) {
            if (log[i] < 0) {
                log[i] += mod;
            }
        }

        PrimitiveBuffers.release(cnt, invBuf);
        int[] exp = poly.exp(log, n + 1);
        ans = Arrays.copyOf(exp, n + 1);

        PrimitiveBuffers.release(exp, log);
    }

    public int[] product(){
        return ans;
    }

    /**
     * Count how many subsets of s exist that the sum of all elements in it equal to i(i <= n), return the number % mod
     */
    public int query(int i) {
        return ans[i];
    }

    @Override
    public String toString() {
        return Arrays.toString(ans);
    }
}
