package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        NumberTheory.Composite comp = new NumberTheory.Composite(n, mod);

        int all = pow.pow(3, n);
        int invalidCnt = 0;
        int p2 = pow.pow(2, n / 2);
        int inv2 = pow.inverse(2);
        for(int i = n / 2 + 1; i <= n; i++){
            p2 = mod.mul(p2, inv2);
            invalidCnt = mod.plus(invalidCnt, mod.mul(comp.composite(n, i), p2));
        }

        invalidCnt = mod.mul(invalidCnt, 2);
        out.println(mod.subtract(all, invalidCnt));
    }
}
