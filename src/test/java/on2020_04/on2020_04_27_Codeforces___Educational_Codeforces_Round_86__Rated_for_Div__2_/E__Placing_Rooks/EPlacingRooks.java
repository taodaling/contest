package on2020_04.on2020_04_27_Codeforces___Educational_Codeforces_Round_86__Rated_for_Div__2_.E__Placing_Rooks;



import combinatorics.Combinations;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.math.PrimeCombination;
import template.primitve.generated.datastructure.IntegerList;

import java.awt.*;

public class EPlacingRooks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Modular mod = new Modular(998244353);
        int m = n - k;

        if (k == 0) {
            int ans = 1;
            for (int i = 1; i <= n; i++) {
                ans = mod.mul(ans, i);
            }
            out.println(ans);
            return;
        }
        if (m <= 0 || m > n) {
            out.println(0);
            return;
        }

        PrimeCombination combination = new PrimeCombination(n, mod);
        Power power = new Power(mod);
        int ans = 0;
        for (int i = 0; i <= m; i++) {
            int local = combination.combination(m, i);
            if (i % 2 == 1) {
                local = mod.valueOf(-local);
            }
            local = mod.mul(local, power.pow(m - i, n));
            ans = mod.plus(ans, local);
        }
        ans = mod.mul(ans, combination.combination(n, m));

        ans = mod.mul(ans, 2);
        out.println(ans);
    }
}
