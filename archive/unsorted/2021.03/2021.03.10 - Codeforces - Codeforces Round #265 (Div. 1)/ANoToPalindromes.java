package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ANoToPalindromes {
    int[] s;
    int[][][][] dp;
    int[][][][] choice;
    int p;

    public boolean dp(int i, int l2, int l1, int floor) {
        if (i == s.length) {
            return floor == 1 ? false : true;
        }
        if (dp[i][l2][l1][floor] == -1) {
            dp[i][l2][l1][floor] = 0;
            for (int j = 0; j < p; j++) {
                if (floor == 1 && s[i] > j) {
                    continue;
                }
                if (l2 == j || l1 == j) {
                    continue;
                }
                if (dp(i + 1, l1, j, floor == 1 && s[i] == j ? 1 : 0)) {
                    dp[i][l2][l1][floor] = 1;
                    choice[i][l2][l1][floor] = j;
                    break;
                }
            }
        }
        return dp[i][l2][l1][floor] == 1;
    }

    public void calc(int i, int l2, int l1, int floor, StringBuilder sb) {
        if (i == s.length) {
            return;
        }
        assert dp[i][l2][l1][floor] == 1;
        int j = choice[i][l2][l1][floor];
        sb.append((char)('a' + j));
        calc(i + 1, l1, j ,floor == 1 && j == s[i] ? 1 : 0, sb);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        p = in.ri();
        s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.rc() - 'a';
        }
        dp = new int[n][p + 1][p + 1][2];
        choice = new int[n][p + 1][p + 1][2];
        SequenceUtils.deepFill(dp, -1);
        if(!dp(0, p, p, 1)){
            out.println("NO");
            return;
        }
        StringBuilder ans = new StringBuilder(n);
        calc(0, p, p, 1, ans);
        out.println(ans);
    }
}
