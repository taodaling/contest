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
        //int[] ans = new CountSubsetSum(new int[]{1, 1, 1}, 3, 998244353, new IntPolyNTT(998244353), (int i) -> i + 1).ans;
        //System.out.println(Arrays.toString(ans));
    }

    int[] ans;
    int mod;



    /**
     * s[i] >= 1 for all i should satisfied. Run in O(|s|+n\log_2n).
     */
    /**
     * another interpretation:
     * given |s| polynomial, F_i = c_i x^{s_i}+1
     * Find F = F_1 * F_2 * ... * F_|S|
     */
    public CountSubsetSum(int[] s, int n, int mod, IntPoly poly) {
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
            for (int j = 1; (ij = i * j) <= n; j++) {
                long invJ = inv.inverse(j);
                long contrib = invJ * cnt[i];
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
