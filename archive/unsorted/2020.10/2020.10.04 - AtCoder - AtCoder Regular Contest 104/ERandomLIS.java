package contest;

import template.algo.LIS;
import template.io.FastInput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ERandomLIS {
    int mod = (int) (1e9 + 7);
    InverseNumber inv = new ModPrimeInverseNumber(10, mod);
    IntRecursiveCombination comb = new IntRecursiveCombination(inv, mod);
    Power power = new Power(mod);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        IntegerArrayList pts = new IntegerArrayList();
        pts.add(0);
        pts.addAll(a);
        pts.unique();
        int[] segs = pts.toArray();


        int[] perm = IntStream.range(0, n).map(i -> i).toArray();
        long way = 0;
        long[][] dp = new long[segs.length][perm.length + 1];
        do {
            int lis = LIS.lisLength(i -> perm[i], n, Integer::compare);
            SequenceUtils.deepFill(dp, 0L);
            dp[0][0] = 1;
            for (int i = 1; i < segs.length; i++) {
                int l = segs[i - 1] + 1;
                int r = segs[i];
                for (int j = 0; j <= perm.length; j++) {
                    dp[i][j] += dp[i - 1][j];
                    int ltCnt = 0;
                    for (int k = j - 1; k >= 0; k--) {
                        int id = perm[k];
                        if (a[id] < r) {
                            break;
                        }
                        if (k < j - 1 && perm[k] < perm[k + 1]) {
                            ltCnt++;
                        }
                        dp[i][j] += method(r - l - ltCnt, j - k) * dp[i - 1][k] % mod;
                    }
                    dp[i][j] %= mod;
                }
            }
            debug.debug("way", dp[segs.length - 1][perm.length]);
            way += lis * dp[segs.length - 1][perm.length] % mod;
        } while (PermutationUtils.nextPermutation(perm));

        long total = 1;
        for (int x : a) {
            total = total * x % mod;
        }

        long ans = way % mod * power.inverse((int) total) % mod;
        out.println(ans);
    }


    //d1+...+dm<=n
    public int method(int n, int m) {
        if (n < 0) {
            return 0;
        }
        return comb.combination(n + m, m);
    }
}