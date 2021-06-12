package on2021_06.on2021_06_11_Codeforces___XXI_Opencup_GP_of_Tokyo.G__Games;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;
import template.polynomial.ModGenericFastWalshHadamardTransform;
import template.utils.Debug;

public class GGames {
    int mod = 998244353;
    IntRadix radix = new IntRadix(7);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long k = in.rl();
        int[] a = in.ri(n);
        int lim = 1;
        for (int i = 0; i < 7; i++) {
            lim = lim * 7;
        }
        int[] cnts = new int[lim];
        for (int x : a) {
            int v = 0;
            for (int i = 0; i < 7; i++) {
                if (Bits.get(x, i) == 1) {
                    v = radix.set(v, i, 1);
                }
            }
            cnts[v]++;
        }
//        debug.debug("g", ModGenericFastWalshHadamardTransform.searchRoot(7, mod));
        ModGenericFastWalshHadamardTransform fwt = new ModGenericFastWalshHadamardTransform(6 + 1, mod, 14553391);

        fwt.addFWTWithRoot(cnts, 0, lim - 1);
        int[] res = new int[lim];
        fwt.pow(cnts, res, 0, lim - 1, k);
        fwt.addIFWTWithRoot(res, 0, lim - 1);
//        fwt.addIFWT(cnts, 0, lim - 1);

        int ans = res[0];
        out.println(ans);
    }
}
