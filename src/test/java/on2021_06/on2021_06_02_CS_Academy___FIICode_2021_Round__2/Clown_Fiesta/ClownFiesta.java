package on2021_06.on2021_06_02_CS_Academy___FIICode_2021_Round__2.Clown_Fiesta;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntegerModPowerLink;

public class ClownFiesta {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2;
        int m = in.ri();
        long[] a = new long[n + 1];
        a[n] = 1;
        for (int i = n - 1; i >= 0; i--) {
            a[i] = in.rl();
        }
        IntegerModPowerLink powerLink = new IntegerModPowerLink(a);
        for (int i = n - 1; i >= 0; i--) {
            int ans = powerLink.query(i, n, m);
            out.println(ans);
        }
    }
}
