package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearFeedbackShiftRegister;
import template.math.Modular;
import template.math.Power;
import template.polynomial.Polynomials;

import java.util.Arrays;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int m = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        int[][] dp = new int[m * 2][m];
        Arrays.fill(dp[0], 1);
        for (int i = 1; i < 2 * m; i++) {
            for (int j = 0; j < m - 1; j++) {
                dp[i][j] = dp[i - 1][j + 1];
            }
            dp[i][m - 1] = mod.plus(dp[i - 1][m - 1], dp[i - 1][0]);
        }
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, 2 * m);
        for (int i = 0; i < 2 * m; i++) {
            lfsr.add(dp[i][0]);
        }
        IntegerList p = new IntegerList(lfsr.length() + 1);
        for (int i = 0; i < lfsr.length(); i++) {
            p.add(mod.valueOf(-lfsr.codeAt(lfsr.length() - i)));
        }
        p.add(1);

        IntegerList remainder = new IntegerList(m * 2);
        Polynomials.module(n, p, remainder, new Power(mod));

        long ans = 0;
        for(int i = 0; i < remainder.size(); i++){
            ans = mod.plus(ans, mod.mul(remainder.get(i), dp[i][0]));
        }

        out.println(ans);
    }
}
