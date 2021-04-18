package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.D___I_Wanna_Win_The_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

import java.util.Arrays;

public class DIWannaWinTheGame {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] prev = new long[m + 1];
        long[] next = new long[m + 1];
        prev[0] = 1;
        for (int i = 12; i >= 0; i--) {
            Arrays.fill(next, 0);
            for (int j = 0; j <= m; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                for (int k = j; k <= m; k += (2 << i)) {
                    int add = (k - j) >> i;
                    next[k] += prev[j] * comb.combination(n, add) % mod;
                }
            }
            for(int j = 0; j <= m; j++){
                next[j] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = prev[m];
        out.println(ans);
    }
}
