package on2020_07.on2020_07_03_AtCoder___AtCoder_Regular_Contest_081.E___Don_t_Be_a_Subsequence;



import template.io.FastInput;
import template.io.FastOutput;

public class EDontBeASubsequence {
    int charset = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 2e5];
        int n = in.readString(s, 0);
        int[][] next = new int[charset][n + 2];
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }

        for (int j = 0; j < charset; j++) {
            next[j][n] = n + 1;
            next[j][n + 1] = n + 1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < charset; j++) {
                next[j][i] = next[j][i + 1];
            }
            next[s[i]][i] = i + 1;
        }

        int[] dp = new int[n + 2];
        dp[n + 1] = 0;
        for (int i = n; i >= 0; i--) {
            dp[i] = n;
            for (int j = 0; j < charset; j++) {
                int go = next[j][i];
                dp[i] = Math.min(dp[i], 1 + dp[go]);
            }
        }

        int index = 0;
        while (index <= n) {
            int step = 0;
            for (int j = 0; j < charset; j++) {
                int go = next[j][index];
                if (dp[go] < dp[next[step][index]]) {
                    step = j;
                }
            }
            out.append((char) (step + 'a'));
            index = next[step][index];
        }
    }

}
