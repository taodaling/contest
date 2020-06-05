package on2020_06.on2020_06_04_Codeforces___Codeforces_Round__647__Div__1____Thanks__Algo_Muse_.B__Johnny_and_Grandmaster;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BJohnnyAndGrandmaster {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int[] k = new int[n];
        boolean[] assignLeft = new boolean[n];
        in.populate(k);
        long sum = 0;

        Randomized.shuffle(k);
        Arrays.sort(k);
        SequenceUtils.reverse(k);
        int level = k[0];
        for (int i = 0; i < n; i++) {
            int x = k[i];
            while (sum >= -n && sum <= n && level > x && p != 1 && sum != 0) {
                level--;
                sum = Math.max(-n * 10, Math.min(n * 10, sum * p));
            }
            level = x;
            if (sum <= 0) {
                sum++;
                assignLeft[i] = true;
            } else {
                sum--;
                assignLeft[i] = false;
            }
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            int val = pow.pow(p, k[i]);
            if (assignLeft[i]) {
                ans = mod.plus(ans, val);
            } else {
                ans = mod.subtract(ans, val);
            }
        }
        if (sum < 0) {
            ans = mod.valueOf(-ans);
        }
        out.println(ans);
    }
}
