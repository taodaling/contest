package on2020_05.on2020_05_24_AtCoder___AtCoder_Grand_Contest_044.D___Guess_the_Password;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class DInteractor extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        String s = input.readString();

        int req = 0;
        while (true) {
            char c = solutionOutput.readChar();
            String ask = solutionOutput.readString();
            if (c == '?') {
                req++;
                if (req > 850) {
                    return Verdict.WA;
                }
                int ans = shortestEditDistance(s, ask);
                solutionInput.println(ans).flush();
            } else {
                return s.equals(ask) ? Verdict.OK : Verdict.WA;
            }
        }
    }

    int[][] dp;

    public int dp(int i, int j) {
        if (i < 0 || j < 0) {
            return Math.max(i + 1, j + 1);
        }
        if (dp[i][j] == -1) {
            dp[i][j] = Math.min(dp(i - 1, j) + 1, dp(i, j - 1) + 1);
            dp[i][j] = Math.min(dp[i][j], dp(i - 1, j - 1) + (a.charAt(i) != b.charAt(j) ? 1 : 0));
        }
        return dp[i][j];
    }

    String a;
    String b;

    public int shortestEditDistance(String a, String b) {
        this.a = a;
        this.b = b;
        int n = a.length();
        int m = b.length();
        dp = new int[n][m];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(n - 1, m - 1);
        return ans;
    }
}
