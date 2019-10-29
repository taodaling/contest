package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;
import template.SubsetGenerator;

public class TaskB {
    int[] cost;
    long[][] dp;
    DigitUtils.DigitBase base3 = new DigitUtils.DigitBase(3);
    DigitUtils.DigitBase base2 = new DigitUtils.DigitBase(2);
    DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
    int n;
    int m;
    int[][] edges;


    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        edges = new int[n][n];
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges[a][b] = edges[b][a] = in.readInt();
        }

        cost = new int[(int) base3.setBit(0, n, 1)];
        dp = new long[(1 << n)][n];
        SequenceUtils.deepFill(cost, -1);
        SequenceUtils.deepFill(dp, -1L);
        buf = new int[n];

        long ans = dp((1 << n) - 1, n - 1);
        out.println(ans);
    }


    int[] buf;

    public int cost(int x) {
        if (cost[x] == -1) {
            base3.decompose(x, buf);
            int cnt1 = 0;
            int cnt2 = 0;
            int one = 0;
            int two = 0;
            for (int i = 0; i < n; i++) {
                if (buf[i] == 1) {
                    cnt1++;
                    one = i;
                }
                if (buf[i] == 2) {
                    cnt2++;
                    two = i;
                }
            }
            if (cnt1 == 0 || cnt2 == 0) {
                cost[x] = 0;
            } else if (cnt1 > 1) {
                int x1 = (int) base3.setBit(x, one, 0);
                for (int i = 0; i < n; i++) {
                    if (buf[i] == 1) {
                        buf[i] = 0;
                    }
                }
                buf[one] = 1;
                int x2 = (int) base3.compose(buf);
                cost[x] = cost(x1) + cost(x2);
            } else if (cnt2 > 1) {
                int x1 = (int) base3.setBit(x, two, 0);
                for (int i = 0; i < n; i++) {
                    if (buf[i] == 2) {
                        buf[i] = 0;
                    }
                }
                buf[two] = 2;
                int x2 = (int) base3.compose(buf);
                cost[x] = cost(x1) + cost(x2);
            } else {
                cost[x] = edges[one][two];
            }
        }
        return cost[x];
    }


    long lInf = (long) 1e10;

    public long dp(int s, int t) {
        if (dp[s][t] == -1) {
            if (bo.bitAt(s, 0) == 0) {
                return dp[s][t] = 0;
            }
            if (t == 0) {
                return dp[s][t] = 0;
            }
            dp[s][t] = lInf;
            int next = bo.setBit(bo.setBit(s, t, false), 0, false);
            SubsetGenerator sg = new SubsetGenerator();
            sg.setSet(next);
            while (sg.hasNext()) {
                int T = bo.setBit(sg.next(), t, true);
                int S = bo.differ(s, T);
                for (int i = 0; i < n; i++) {
                    if (bo.bitAt(S, i) == 1) {
                        buf[i] = 1;
                    } else if (bo.bitAt(T, i) == 1) {
                        buf[i] = 2;
                    } else {
                        buf[i] = 0;
                    }
                }

                int c = cost((int) base3.compose(buf));
                for (int i = 0; i < n; i++) {
                    if (edges[i][t] == 0 || bo.bitAt(S, i) == 0) {
                        continue;
                    }
                    dp[s][t] = Math.min(dp[s][t], dp(bo.setBit(S, i, false), i) + c - edges[i][t]);
                }
            }

        }
        return dp[s][t];
    }
}
