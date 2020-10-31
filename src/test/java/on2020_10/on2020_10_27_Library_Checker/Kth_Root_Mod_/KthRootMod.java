package on2020_10.on2020_10_27_Library_Checker.Kth_Root_Mod_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModPrimeRoot;

public class KthRootMod {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int y = in.readInt();
        int p = in.readInt();
        ModPrimeRoot modPrimeRoot = new ModPrimeRoot(p);
        int ans = modPrimeRoot.root(y, k);
        assert ans == -1 || DigitUtils.modPow(ans, k, p) == y;
        out.println(ans);
    }
}
