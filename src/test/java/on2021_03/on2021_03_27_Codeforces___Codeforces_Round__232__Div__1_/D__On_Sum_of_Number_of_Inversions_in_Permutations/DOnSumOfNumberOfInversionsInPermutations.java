package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__232__Div__1_.D__On_Sum_of_Number_of_Inversions_in_Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

public class DOnSumOfNumberOfInversionsInPermutations {
    int mod = (int) 1e9 + 7;
    long[][] dp;
    long[][] way;
    int[] rank;
    int n;

    public long dp(int i, int ceil) {
        if (i == n) {
            return 0;
        }
        if (dp[ceil][i] == -1) {
            int choice = n - i;
            long ans = 0;
            if (ceil == 0) {
                //0,1,2,...,choice-1
                ans += way(i + 1, 0) * (IntMath.sumOfInterval(0, choice - 1) % mod);
                ans += dp(i + 1, 0) * choice;
            } else {
                //0,1,2,...,rank[i]-2
                ans += way(i + 1, 0) * (IntMath.sumOfInterval(0, rank[i] - 2) % mod);
                ans += dp(i + 1, 0) * (rank[i] - 1);
                ans += way(i + 1, 1) * (rank[i] - 1);
                ans += dp(i + 1, 1);
            }
            ans %= mod;
            dp[ceil][i] = ans;
        }
        return dp[ceil][i];
    }

    public long way(int i, int ceil) {
        if (i == n) {
            return 1;
        }
        if (way[ceil][i] == -1) {
            int choice = n - i;
            long ans = 0;
            if (ceil == 0) {
                ans += way(i + 1, 0) * choice;
            } else {
                ans += way(i + 1, 0) * (rank[i] - 1) + way(i + 1, 1);
            }
            ans %= mod;
            way[ceil][i] = ans;
        }
        return way[ceil][i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int[] p = in.ri(n);
        IntegerBIT bit = new IntegerBIT(n);
        rank = new int[n];
        dp = new long[2][n];
        way = new long[2][n];
        for (int i = n - 1; i >= 0; i--) {
            rank[i] = bit.query(p[i]) + 1;
            bit.update(p[i], 1);
        }
        SequenceUtils.deepFill(dp, -1L);
        SequenceUtils.deepFill(way, -1L);
        long ans = dp(0, 1);
        out.println(ans);
    }
}
