package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;

public class StringRemovals {
    int mod = (int) 1e9 + 7;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 5e5];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        int charset = 'z' - 'a' + 1;
        int[] pre = new int[n];
        int[] reg = new int[charset];
        for (int i = 0; i < n; i++) {
            pre[i] = reg[s[i]];
            reg[s[i]] = i;
        }
        long[] dp = new long[n];
        long[] ps = new long[n];
        for (int i = 0; i < n; i++) {
            long cnt = 0;
            if(i > 0){
                cnt += ps[i - 1];
            }
            if(pre[i] == i || s[pre[i]] != s[i]){
                cnt++;
            }
            if (pre[i] > 0) {
                cnt -= ps[pre[i] - 1];
            }
            dp[i] = DigitUtils.mod(cnt, mod);
            ps[i] = dp[i];
            if (i > 0) {
                ps[i] = DigitUtils.modplus(ps[i - 1], ps[i], mod);
            }
        }

        debug.debug("dp", dp);
        debug.debug("ps", ps);
        long ans = ps[n - 1];
        out.println(ans);
    }
}
