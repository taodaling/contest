package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearRecurrenceSolver;
import template.math.ModMatrix;
import template.math.ModPrimeRoot;
import template.math.Modular;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(998244353);
        int k = in.readInt();
        int[] b = new int[k];
        for (int i = 0; i < k; i++) {
            b[i] = in.readInt();
        }
        int n = in.readInt();
        int m = in.readInt();

        IntegerList coes = new IntegerList();
        coes.addAll(b);
        IntegerList a = new IntegerList();
        a.expandWith(0, k);
        a.set(k - 1, 1);
        LinearRecurrenceSolver solver = LinearRecurrenceSolver
                .newSolverFromLinearRecurrence(coes, mod.getModularForPowerComputation());
        int t = solver.solve(n - 1, a);
        ModPrimeRoot root = new ModPrimeRoot(mod);
        int ans = root.root(m, t);
        out.println(ans);
    }
}
