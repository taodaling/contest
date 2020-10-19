package contest;

import template.datastructure.MonotoneOrder;
import template.io.FastInput;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class CCamelsAndBridge {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] w = new int[n];
        in.populate(w);
        int maxW = Arrays.stream(w).max().orElse(0);
        int inf = (int) 1e9;
        mo = new MonotoneOrder<>(Comparator.<Integer>naturalOrder(), Comparator.<Integer>naturalOrder(), 0);
        dp = new int[n];
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int v = in.readInt();
            mo.add(v + 1, l);
            if (v < maxW) {
                out.println(-1);
                return;
            }
        }
        int[] perm = IntStream.range(0, n).toArray();
        IntegerPreSum ps = new IntegerPreSum(n);
        int ans = inf;
        do {
            ps.populate(i -> w[perm[i]], n);
            Arrays.fill(dp, 0);
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    int total = ps.intervalSum(j, i);
                    int atLeast = mo.atLeast(total);
                    dp[i] = Math.max(dp[i], dp[j] + atLeast);
                }
            }
            ans = Math.min(ans, dp[n - 1]);
        } while (PermutationUtils.nextPermutation(perm));
        out.println(ans);
    }

    int[] dp;
    MonotoneOrder<Integer, Integer> mo;
}
