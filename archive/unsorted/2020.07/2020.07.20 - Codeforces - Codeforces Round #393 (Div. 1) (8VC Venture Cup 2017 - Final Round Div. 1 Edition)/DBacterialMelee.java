package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;
import template.math.Power;
import template.utils.ArrayIndex;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBacterialMelee {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = in.readChar();
            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != c) {
                sb.append(c);
            }
        }
        int m = sb.length();
        int[] seq = new int[sb.length() + 1];
        for (int i = 1; i <= sb.length(); i++) {
            seq[i] = sb.charAt(i - 1) - 'a';
        }
        seq[0] = -1;
        //debug.debug("seq", seq);
        int charset = 'z' - 'a' + 1;
        int[][] next = new int[m + 1][charset];
        Arrays.fill(next[m], m + 1);
        for (int i = m - 1; i >= 0; i--) {
            for (int j = 0; j < charset; j++) {
                next[i][j] = next[i + 1][j];
            }
            next[i][seq[i + 1]] = i + 1;
        }
        debug.elapse("init");
        //debug.debug("next", next);

        int[][] dp = new int[m + 1][m + 1];
        dp[0][0] = 1;
        Modular mod = new Modular(1e9 + 7);
        int[][] sum = new int[charset][m + 1];
        int[] global = new int[m + 1];
        for (int i = 0; i <= m; i++) {
            int c = seq[i];
            if (i > 0) {
                for (int j = 0; j <= i; j++) {
                    dp[i][j] = mod.plus(sum[c][j], global[j]);
                    sum[c][j] = mod.valueOf(-global[j]);
                }
            }
            if (i == m) {
                break;
            }
            for (int j = 0; j <= i; j++) {
                global[j + 1] = mod.plus(global[j + 1], dp[i][j]);
                if (c >= 0) {
                    sum[c][j + 1] = mod.subtract(sum[c][j + 1], dp[i][j]);
                }
            }
        }
        debug.elapse("dp");

        int[] cnts = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= m; j++) {
                cnts[j] = mod.plus(cnts[j], dp[i][j]);
            }
        }
        //debug.debug("cnts", cnts);

        int ans = 0;
        Power pow = new Power(mod);
        Combination comb = new Combination(n, pow);
        for (int i = 1; i <= m; i++) {
            int contrib = comb.combination(n - i + i - 1, i - 1);
            contrib = mod.mul(contrib, cnts[i]);
            ans = mod.plus(ans, contrib);
        }

        debug.elapse("calc ans");
        out.println(ans);
    }
}
