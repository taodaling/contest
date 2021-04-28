package on2021_04.on2021_04_27_Codeforces___Codeforces_Round__196__Div__1_.D__GCD_Table;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExtCRT;
import template.math.GCDs;
import template.utils.Debug;

public class DGCDTable {
    long inf = (long) 1e18;

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long m = in.rl();
        int k = in.ri();
        long[] a = in.rl(k);
        long lcm = 1;
        for (long x : a) {
            long g = GCDs.gcd(lcm, x);
            lcm = DigitUtils.mul(lcm, x / g, inf);
        }
        String yes = "YES";
        String no = "NO";
        if (lcm > n) {
            out.println(no);
            return;
        }
        ExtCRT crt = new ExtCRT();
        for (int i = 0; i < k; i++) {
            crt.add(DigitUtils.mod(-i, a[i]), a[i]);
        }
        if (!crt.valid()) {
            out.println(no);
            return;
        }
        long j = crt.getValue();
        if (j == 0) {
            j += lcm;
        }
        if (j + k - 1 > m) {
            out.println(no);
            return;
        }
        for (int i = 0; i < k; i++) {
            long v = j + i;
            if (GCDs.gcd(v, lcm) != a[i]) {
                out.println(no);
                return;
            }
        }
        out.println(yes);
    }
}
