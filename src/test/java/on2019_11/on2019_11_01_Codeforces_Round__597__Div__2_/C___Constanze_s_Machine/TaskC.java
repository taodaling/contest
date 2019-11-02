package on2019_11.on2019_11_01_Codeforces_Round__597__Div__2_.C___Constanze_s_Machine;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[100001];
        int n = in.readString(s, 1);
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);

        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            if (s[i] == 'm' || s[i] == 'w') {
                out.println(0);
                return;
            }
        }

        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1];
            if (s[i] == 'n' && s[i - 1] == 'n' || s[i] == 'u' && s[i - 1] == 'u') {
                dp[i] = mod.plus(dp[i], dp[i - 2]);
            }
        }

        out.println(dp[n]);
    }
}
