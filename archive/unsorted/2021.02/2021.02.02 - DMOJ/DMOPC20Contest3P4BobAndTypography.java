package contest;

import template.datastructure.MonotoneOrderBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Comparator;
import java.util.Map;

public class DMOPC20Contest3P4BobAndTypography {
    public int[] dp(int[] a) {
        int n = a.length;
        MonotoneOrderBeta<Integer, Integer>[] dp = new MonotoneOrderBeta[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += a[i];
            dp[i] = new MonotoneOrderBeta<Integer, Integer>(Comparator.naturalOrder(),
                    Comparator.naturalOrder(), false, true);
            dp[i].add(sum, 1);
        }

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = dp[i].first().getValue();
            int lastJ = i;
            sum = 0;
            for (Map.Entry<Integer, Integer> entry : dp[i]) {
                int last = entry.getKey();
                int len = entry.getValue();
                while (lastJ + 1 < n && sum + a[lastJ + 1] <= last) {
                    lastJ++;
                    sum += a[lastJ];
                    dp[lastJ].add(sum, len + 1);
                }
            }
        }

        return ans;
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] left = dp(a);
        SequenceUtils.reverse(a);
        int[] right = dp(a);
        SequenceUtils.reverse(right);
        debug.debug("left", left);
        debug.debug("right", right);
        int ans = 0;
        for (int i = 0; i < n - 1; i++) {
            int cand = left[i] + right[i + 1];
            ans = Math.max(ans, cand);
        }
        out.println(ans);
    }
}

class State {
    int sum;
    int len;

    public State(int sum, int len) {
        this.sum = sum;
        this.len = len;
    }
}
