package on2020_04.on2020_04_16_Codeforces___Codeforces_Round__635__Div__1_.C__Kaavi_and_Magic_Spell;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class CKaaviAndMagicSpell {
    int[] a = new int[3000];
    int[] b = new int[3000];
    int n;
    int m;

    //is x match b[i]
    public boolean match(int x, int i) {
        if (i < 0) {
            return false;
        }
        if (i >= m) {
            return true;
        }
        return b[i] == x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {

        n = in.readString(a, 0);
        m = in.readString(b, 0);

        Modular mod = new Modular(998244353);
        int[][] dp = new int[n + 1][n + 2];
        Arrays.fill(dp[0], 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n + 1; j++) {
                int front = j - 1;
                int back = j + i;
                //put front
                if (front >= 0 && match(a[i], front)) {
                    dp[i + 1][front] = mod.plus(dp[i + 1][front], dp[i][j]);
                }
                //put back
                if (match(a[i], back)) {
                    dp[i + 1][j] = mod.plus(dp[i + 1][j], dp[i][j]);
                }
            }
        }

        int ans = 0;
        for(int i = m; i <= n; i++){
            ans = mod.plus(ans, dp[i][0]);
        }

        out.println(ans);
    }
}
