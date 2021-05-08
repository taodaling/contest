package on2021_05.on2021_05_07_Codeforces___Codeforces_Round__189__Div__1_.C__Kalila_and_Dimna_in_the_Logging_Industry;



import template.geometry.old.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;

public class CKalilaAndDimnaInTheLoggingIndustry {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        LongConvexHullTrick cht = new LongConvexHullTrick();
        long[] dp = new long[n];
        dp[0] = 0;
        cht.insert(-b[0], -dp[0]);
        for(int i = 1; i < n; i++){
            dp[i] = -cht.query(a[i]);
            cht.insert(-b[i], -dp[i]);
        }
        long ans = dp[n - 1];
        out.println(ans);
    }
}
