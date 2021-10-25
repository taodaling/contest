package on2021_09.on2021_09_26_AtCoder___AtCoder_Regular_Contest_127.A___Leading_1s;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.utils.SequenceUtils;

public class ALeading1s {

    long[][][] dp = new long[20][2][3];
    long[][][] way = new long[20][2][3];
    long N;
    LongRadix radix = new LongRadix(10);

    public long dp(int p, int ceil, int state) {
        if (p < 0) {
            return 0;
        }
        if (dp[p][ceil][state] == -1) {
            dp[p][ceil][state] = 0;
            int num = radix.get(N, p);
            for (int i = 0; i < 10; i++) {
                if (i > num && ceil == 1) {
                    continue;
                }
                int nceil = ceil == 1 && i == num ? 1 : 0;
                int nstate = state;
                if (state == 0) {
                    if (i > 0) {
                        nstate = i == 1 ? 1 : 2;
                    }
                } else if (state == 1) {
                    nstate = i == 1 ? 1 : 2;
                } else {
                    nstate = 2;
                }
                dp[p][ceil][state] += dp(p - 1, nceil, nstate) + way(p - 1, nceil, nstate) * (nstate == 1 ? 1 : 0);
            }
        }
        return dp[p][ceil][state];
    }

    public long way(int p, int ceil, int state) {
        if (p < 0) {
            return 1;
        }
        if (way[p][ceil][state] == -1) {
            way[p][ceil][state] = 0;
            int num = radix.get(N, p);
            for (int i = 0; i < 10; i++) {
                if (i > num && ceil == 1) {
                    continue;
                }
                int nceil = ceil == 1 && i == num ? 1 : 0;
                int nstate = state;
                if (state == 0) {
                    if (i > 0) {
                        nstate = i == 1 ? 1 : 2;
                    }
                } else if (state == 1) {
                    nstate = i == 1 ? 1 : 2;
                } else {
                    nstate = 2;
                }
                way[p][ceil][state] += way(p - 1, nceil, nstate);
            }
        }
        return way[p][ceil][state];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        SequenceUtils.deepFill(dp, -1L);
        SequenceUtils.deepFill(way, -1L);
        N = in.rl();
        long ans = dp(19, 1, 0);
        out.println(ans);
    }
}
