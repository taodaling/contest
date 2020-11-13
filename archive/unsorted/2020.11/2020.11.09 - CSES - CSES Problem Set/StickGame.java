package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class StickGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] op = new int[k];
        in.populate(op);
        boolean[] dp = new boolean[n + 1];
        dp[0] = false;
        for(int i = 1; i <= n; i++){
            boolean ans = false;
            for(int x : op){
                if(i >= x){
                    ans = ans || !dp[i - x];
                }
            }
            dp[i] = ans;
            out.append(dp[i] ? 'W' : 'L');
        }
    }
}
