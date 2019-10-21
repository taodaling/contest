package on2019_10.on2019_10_20_Codeforces_Round__594__Div__1_.A___Ivan_the_Fool_and_the_Probability_Theory;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        if (n > m) {
            int tmp = n;
            n = m;
            m = tmp;
        }

        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);

        int[] dp = new int[m + 1];
        dp[0] = 1;
        for(int i = 1; i <= m; i++){
            dp[i] = dp[i - 1];
            if(i > 1){
                dp[i] = mod.plus(dp[i], dp[i - 2]);
            }
        }

        int ans1 = mod.mul(mod.subtract(dp[n], 1), 2);
        int ans2 = mod.mul(mod.subtract(dp[m], 1), 2);
        int ans3 = 2;

        int ans = mod.plus(ans1, ans2);
        ans = mod.plus(ans, ans3);
        out.println(ans);
    }
}
