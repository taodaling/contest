package on2020_10.on2020_10_27_Library_Checker.Kth_Root_Mod_0;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KthRootModPrime;
import template.math.ModPrimeRoot;

public class KthRootMod {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int y = in.readInt();
        int p = in.readInt();
        KthRootModPrime modPrimeRoot = new KthRootModPrime();
        long ans = modPrimeRoot.kth_root(y, k, p);
        assert ans == -1 || DigitUtils.modPow((int)ans, k, p) == y;
        out.println(ans);
    }
}
