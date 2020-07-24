package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRecursiveCombination;
import template.math.Lucas;
import template.math.Modular;
import template.math.Power;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(10007);
        Power pow = new Power(mod);
        IntRecursiveCombination comb = new IntRecursiveCombination(pow);
        Lucas lucas = new Lucas(comb, mod.getMod());
        int n = (int) in.readModInt(mod.getMod());
        out.println(lucas.combination(n + 2, 3));
    }
}
