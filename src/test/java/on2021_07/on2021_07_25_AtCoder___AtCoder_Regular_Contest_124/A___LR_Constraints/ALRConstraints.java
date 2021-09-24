package on2021_07.on2021_07_25_AtCoder___AtCoder_Regular_Contest_124.A___LR_Constraints;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ALRConstraints {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();

        int[] must = new int[n];
        Arrays.fill(must, -1);
        int[] possible = new int[n + 1];
        for (int i = 0; i < k; i++) {
            char c = in.rc();
            int x = in.ri() - 1;
            if (must[x] != -1) {
                out.println(0);
                return;
            }
            must[x] = i;
            if (c == 'L') {
                possible[x]++;
            } else {
                possible[0]++;
                possible[x + 1]--;
            }
        }
        for (int i = 1; i <= n; i++) {
            possible[i] += possible[i - 1];
        }
        long ans = 1;
        for (int i = 0; i < n; i++) {
            if (must[i] == -1) {
                ans = ans * possible[i] % mod;
            }
        }
        out.println(ans);
    }
}
