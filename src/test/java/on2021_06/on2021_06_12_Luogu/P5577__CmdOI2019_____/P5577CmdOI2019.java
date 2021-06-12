package on2021_06.on2021_06_12_Luogu.P5577__CmdOI2019_____;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.ModGenericFastWalshHadamardTransform;
import template.utils.Debug;

public class P5577CmdOI2019 {
    int mod = 998244353;
    int k;

    public int asK(int x) {
        if (x == 0) {
            return 0;
        }
        return asK(x / 10) * k + x % 10;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        k = in.ri();
        int m = in.ri();
        int km = 1;
        for (int i = 0; i < m; i++) {
            km *= k;
        }
        int[] C = new int[km];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            C[asK(x)]++;
        }
       //Debug debug = new Debug(true);
        //debug.debug("support", ModGenericFastWalshHadamardTransform.currentSupport());
        ModGenericFastWalshHadamardTransform fwt = new ModGenericFastWalshHadamardTransform(k, mod);
        int[] ans = fwt.countSubset(C);
        for (int i = 0; i < ans.length; i++) {
            out.println(ans[i]);
        }
    }
}
