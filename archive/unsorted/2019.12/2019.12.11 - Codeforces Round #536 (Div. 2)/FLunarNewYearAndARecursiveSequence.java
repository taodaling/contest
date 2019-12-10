package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.ModPrimeRoot;
import template.math.Modular;

public class FLunarNewYearAndARecursiveSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int[] b = new int[k];
        for (int i = 0; i < k; i++) {
            b[i] = in.readInt();
        }
        int n = in.readInt();
        int m = in.readInt();

        Modular mod = new Modular(998244353);
        Modular powMod = mod.getModularForPowerComputation();

        ModMatrix mat = new ModMatrix(k, k);
        for(int i = 0; i < k; i++){
            mat.set(k - 1,  i, b[k - i - 1]);
        }
        for(int i = k - 2; i >= 0; i--){
            mat.set(i, i + 1, 1);
        }

        ModMatrix init = new ModMatrix(k, 1);
        init.set(k - 1, 0, 1);

        ModMatrix finalState = ModMatrix.mul(ModMatrix.pow(mat, n - 1, powMod), init, powMod);
        int t = finalState.get(0, 0);

        ModPrimeRoot root = new ModPrimeRoot(mod, 3);
        int ans = root.root(m, t);
        out.println(ans);
    }
}
