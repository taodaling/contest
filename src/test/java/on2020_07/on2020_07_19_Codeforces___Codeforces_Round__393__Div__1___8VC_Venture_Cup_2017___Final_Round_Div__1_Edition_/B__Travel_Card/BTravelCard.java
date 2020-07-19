package on2020_07.on2020_07_19_Codeforces___Codeforces_Round__393__Div__1___8VC_Venture_Cup_2017___Final_Round_Div__1_Edition_.B__Travel_Card;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class BTravelCard {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] time = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            time[i] = in.readInt();
        }
        long[] dp = new long[n + 1];
        int l1 = 1;
        int l2 = 1;
        for (int i = 1; i <= n; i++) {
            dp[i] = (long) 1e18;
            dp[i] = Math.min(dp[i], dp[i - 1] + 20);

            while (l1 < i && time[l1] + 90 - 1 < time[i]) {
                l1++;
            }
            while (l2 < i && time[l2] + 1440 - 1 < time[i]) {
                l2++;
            }
            if (l1 < i) {
                dp[i] = Math.min(dp[i], dp[l1 - 1] + 50);
            }
            if(l2 < i){
                dp[i] = Math.min(dp[i], dp[l2 - 1] + 120);
            }
        }

        debug.debug("dp", dp);
        for(int i = 1; i <= n; i++){
            out.println(dp[i] - dp[i - 1]);
        }
    }
}
