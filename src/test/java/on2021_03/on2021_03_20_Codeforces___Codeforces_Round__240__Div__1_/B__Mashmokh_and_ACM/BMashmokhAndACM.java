package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__240__Div__1_.B__Mashmokh_and_ACM;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BMashmokhAndACM {
    int mod = (int)1e9 + 7;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] prev = new long[n + 1];
        long[] next = new long[n + 1];
        prev[1] = 1;
        for(int i = 0; i < k; i++){
            Arrays.fill(next, 0);
            for(int j = 1; j <= n; j++){
                for(int t = j; t <= n; t += j){
                    next[t] += prev[j];
                }
            }
            for(int j = 0; j <= n; j++){
                next[j] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = 0;
        for(long x : prev){
            ans += x;
        }
        ans %= mod;
        out.println(ans);
    }
}
