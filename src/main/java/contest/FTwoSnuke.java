package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRecursiveCombination;
import template.math.Lucas;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

public class FTwoSnuke {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    IntRecursiveCombination comb = new IntRecursiveCombination(pow);
    Lucas lucas = new Lucas(comb, mod.getMod());

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = n - 5;
        if(m < 0){
            out.println(0);
            return;
        }
        int ans = lucas.combination(m + 11 - 1, 11 - 1);
        out.println(ans);

        Runtime.getRuntime().addShutdownHook();
    }
}
