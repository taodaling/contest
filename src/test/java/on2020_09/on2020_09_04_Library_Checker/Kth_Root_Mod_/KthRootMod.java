package on2020_09.on2020_09_04_Library_Checker.Kth_Root_Mod_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeRoot;
import template.math.Modular;
import template.math.RelativePrimeModLog;
import template.utils.Debug;

public class KthRootMod {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int y = in.readInt();
        int p = in.readInt();

        ModPrimeRoot root = new ModPrimeRoot(new Modular(p));
        int ans = root.root(y, k);
        out.println(ans);

//        debug.debug("ct", RelativePrimeModLog.constructTime / 1000000);
//        debug.debug("lt", RelativePrimeModLog.logTime / 1000000);

    }
}
