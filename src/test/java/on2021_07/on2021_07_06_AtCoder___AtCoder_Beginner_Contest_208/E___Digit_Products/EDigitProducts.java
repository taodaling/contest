package on2021_07.on2021_07_06_AtCoder___AtCoder_Beginner_Contest_208.E___Digit_Products;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.utils.SequenceUtils;

public class EDigitProducts {
    char[] N;
    int k;
    long[][][][][] dp;
    int L = 40000;

    public long dp(int pos, int ceil, int state, int begin, int val) {
        if (pos >= N.length) {
            if (begin == 0) {
                return 0;
            }
            //mul
            if (state == 0) {
                return val <= k ? 1 : 0;
            } else {
                return val > 0 ? 1 : 0;
            }
        }
        if (dp[pos][ceil][state][begin][val] == -1) {
            int cur = N[pos] - '0';
            long ans = 0;
            for (int i = 0; i < 10; i++) {
                if (ceil == 1 && i > cur) {
                    continue;
                }
                if (i == 0 && begin == 0) {
                    ans += dp(pos + 1, ceil == 1 && i == cur ? 1 : 0,
                            0, 0, 1);
                    continue;
                }
                int nextVal;
                int nextState = state;
                if (state == 0) {
                    nextVal = val * i;
                    if (nextVal >= L) {
                        nextState = 1;
                        nextVal = k / nextVal;
                    }
                } else {
                    if (i != 0) {
                        nextVal = val / i;
                    } else {
                        nextVal = 0;
                        nextState = 0;
                    }
                }
                ans += dp(pos + 1, ceil == 1 && i == cur ? 1 : 0,
                        nextState, begin == 1 || i > 0 ? 1 : 0, nextVal);
            }
            dp[pos][ceil][state][begin][val] = ans;
        }
        return dp[pos][ceil][state][begin][val];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        N = in.rs().toCharArray();
        k = in.ri();
        dp = new long[N.length][2][2][2][L];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, 1, 0, 0, 1);
        out.println(ans);
    }
}
