package on2020_04.on2020_04_27_Kattis.Vje_tica;



import template.algo.SubsetGenerator;
import template.binary.Log2;
import template.datastructure.PreXor;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Vjetica {
    int charset = 'z' - 'a' + 1;
    int mask;
    int inf = (int) 1e9;
    int[][] min;
    int[] sum;
    int[] dp;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[1000000];

        Prefix[] ps = new Prefix[n];
        for (int i = 0; i < n; i++) {
            int len = in.readString(s, 0);
            ps[i] = new Prefix();
            for (int j = 0; j < len; j++) {
                ps[i].cnts[s[j] - 'a']++;
            }
        }

        mask = (1 << n) - 1;
        min = new int[mask + 1][charset];
        sum = new int[mask + 1];
        Arrays.fill(min[0], inf);
        for (int i = 1; i <= mask; i++) {
            int lowestBit = Integer.lowestOneBit(i);
            int j = i - lowestBit;
            int log = Log2.floorLog(lowestBit);
            for (int k = 0; k < charset; k++) {
                min[i][k] = Math.min(min[j][k], ps[log].cnts[k]);
                sum[i] += min[i][k];
            }
        }

        dp = new int[mask + 1];
        Arrays.fill(dp, -1);

        int ans = dp(mask) + 1;
        out.println(ans);
    }

    public int dp(int s) {
        if (s == 0) {
            return 0;
        }
        if (dp[s] == -1) {
            dp[s] = inf;
            if (Integer.lowestOneBit(s) == s) {
                dp[s] = Math.min(dp[s], sum[s]);
            }
            int subset = s;
            while (subset != 0) {
                subset = (subset - 1) & s;
                if (subset == 0) {
                    continue;
                }
                dp[s] = Math.min(dp[s], dp(subset) + dp(s - subset) - sum[s]);
            }
        }
        return dp[s];
    }
}

class Prefix {
    int[] cnts = new int['z' - 'a' + 1];
}