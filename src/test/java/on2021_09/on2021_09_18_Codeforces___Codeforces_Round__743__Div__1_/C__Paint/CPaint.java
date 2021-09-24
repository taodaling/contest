package on2021_09.on2021_09_18_Codeforces___Codeforces_Round__743__Div__1_.C__Paint;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CPaint {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            int x = in.ri() - 1;
            if(!list.isEmpty() && list.tail() == x){
                continue;
            }
            list.add(x);
        }
        int[] a = list.toArray();
        int m = a.length;
        next = new int[m];
        int[] reg = new int[n];
        Arrays.fill(reg, m);
        for (int i = m - 1; i >= 0; i--) {
            next[i] = reg[a[i]];
            reg[a[i]] = i;
        }
        dp = new int[m][m];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(0, m - 1);
        debug.debug("ans", ans);
        out.println(m - 1 - ans);
    }

    int n;
    int[] next;
    int[][] dp;
    Debug debug = new Debug(false);
    public int dp(int l, int r) {
        if (l >= r) {
            return 0;
        }
        if (dp[l][r] == -1) {
            int ans = dp(l + 1, r);
            int iter = l;
            while(next[iter] <= r){
                iter = next[iter];
                ans = Math.max(ans, dp(l + 1, iter - 1) + dp(iter, r) + 1);
            }
            dp[l][r] = ans;
        }
        return dp[l][r];
    }
}
