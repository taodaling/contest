package on2021_07.on2021_07_22_Codeforces___2020_Petrozavodsk_Winter_Camp__Jagiellonian_U_Contest.L__Wizards_Unite;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class LWizardsUnite {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] t = in.ri(n);
        Randomized.shuffle(t);
        Arrays.sort(t);
        long ans = 0;
        for (int i = 0; i < n - k; i++) {
            ans += t[i];
        }
        ans = Math.max(ans, t[n - 1]);
        out.println(ans);
    }
}
