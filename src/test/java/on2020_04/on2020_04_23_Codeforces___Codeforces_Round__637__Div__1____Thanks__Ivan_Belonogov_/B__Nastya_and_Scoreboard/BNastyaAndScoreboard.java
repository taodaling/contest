package on2020_04.on2020_04_23_Codeforces___Codeforces_Round__637__Div__1____Thanks__Ivan_Belonogov_.B__Nastya_and_Scoreboard;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BNastyaAndScoreboard {
    //Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] bits = new int[10];
        bits[0] = parse(123567);
        bits[1] = parse(36);
        bits[2] = parse(13457);
        bits[3] = parse(13467);
        bits[4] = parse(2346);
        bits[5] = parse(12467);
        bits[6] = parse(124567);
        bits[7] = parse(136);
        bits[8] = parse(1234567);
        bits[9] = parse(123467);

//        for (int i = 0; i < 10; i++) {
//            debug.debug("i", i);
//            debug.debug("bits[i]", Integer.toString(bits[i], 2));
//        }

        int[] now = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 7; j++) {
                if (in.readChar() == '1') {
                    now[i] |= 1 << j;
                }
            }
//            debug.debug("i", i);
//            debug.debug("now[i]", Integer.toString(now[i], 2));
        }

        SequenceUtils.reverse(now, 0, n - 1);
        int[][] dp = new int[n + 1][k + 1];
        SequenceUtils.deepFill(dp, -1);
        dp[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            int v = now[i - 1];
            for (int j = 0; j < 10; j++) {
                if ((bits[j] | v) != bits[j]) {
                    continue;
                }
                int change = Integer.bitCount(bits[j] - v);
                for (int t = change; t <= k; t++) {
                    if (dp[i - 1][t - change] >= 0) {
                        dp[i][t] = j;
                    }
                }
            }
        }


      //  debug.debug("dp", dp);

        if (dp[n][k] == -1) {
            out.println(-1);
            return;
        }


        int j = k;
        for (int i = n; i >= 1; i--) {
            int v = now[i - 1];
            out.append(dp[i][j]);
            j -= Integer.bitCount(bits[dp[i][j]] - v);
        }

    }

    public int parse(int val) {
        if (val == 0) {
            return 0;
        }
        return parse(val / 10) | (1 << (val % 10 - 1));
    }
}
