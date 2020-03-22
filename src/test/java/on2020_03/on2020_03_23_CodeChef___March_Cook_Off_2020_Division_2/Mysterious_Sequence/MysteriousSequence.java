package on2020_03.on2020_03_23_CodeChef___March_Cook_Off_2020_Division_2.Mysterious_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;

public class MysteriousSequence {
    Modular mod = new Modular(1e9 + 7);
    CachedPow pow2 = new CachedPow(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] b = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            b[i] = in.readInt();
        }
        boolean valid = true;
        for (int i = 1; i <= n; i++) {
            if ((b[i] | b[i - 1]) != b[i]) {
                valid = false;
            }
        }
        if (!valid) {
            out.println(0);
            return;
        }

        int ans = 1;
        for (int i = 1; i <= n; i++) {
            ans = mod.mul(ans, pow2.pow(Integer.bitCount(b[i - 1])));
        }

        out.println(ans);
    }
}
