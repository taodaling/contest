package on2019_11.on2019_11_15_diverta_2019_Programming_Contest_2.E___Balanced_Piles;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int h = in.readInt();
        int d = in.readInt();
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Factorial fact = new NumberTheory.Factorial(n, mod);
        int[] dp = new int[h + 1];
        dp[0] = fact.fact(n);
        int factSum = 0;
        for(int i = 1; i <= n; i++){
            factSum = mod.plus(factSum, fact.fact(i));
        }
        int[] preDpSum = new int[h + 1];
        preDpSum[0] = dp[0];
        for(int i = 1; i <= h; i++){
            dp[i] = preDpSum[i - 1];
            if(i - d - 1 >= 0){
                dp[i] = mod.subtract(dp[i], preDpSum[i - d - 1]);
            }
            if(i < h){
                dp[i] = mod.mul(dp[i], factSum);
            }
            preDpSum[i] = mod.plus(preDpSum[i - 1], dp[i]);
        }

        out.println(dp[h]);
    }
}
