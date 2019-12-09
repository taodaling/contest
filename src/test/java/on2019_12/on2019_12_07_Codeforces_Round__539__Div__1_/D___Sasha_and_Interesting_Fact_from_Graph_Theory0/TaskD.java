package on2019_12.on2019_12_07_Codeforces_Round__539__Div__1_.D___Sasha_and_Interesting_Fact_from_Graph_Theory0;





import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.math.Modular;
import template.math.Power;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
        }
        BitOperator bo = new BitOperator();
        int[] cnts = new int[64];
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        for (int i = 0; i < 64; i++) {
            for (long x : a) {
                cnts[i] += bo.bitAt(x, i);
            }
        }

        int ans = 0;
        for (long x : a) {
            for (int i = 0; i < 64; i++) {
                int bit = bo.bitAt(x, i);
                if (bit == 1) {
                    ans = mod.plus(ans, mod.mul(n - cnts[i], mod.valueOf(1L << i)));
                }else{
                    ans = mod.plus(ans, mod.mul(cnts[i], mod.valueOf(1L << i)));
                }
            }
        }

        ans = mod.mul(ans, power.inverse(2));
        out.println(ans);
    }
}
