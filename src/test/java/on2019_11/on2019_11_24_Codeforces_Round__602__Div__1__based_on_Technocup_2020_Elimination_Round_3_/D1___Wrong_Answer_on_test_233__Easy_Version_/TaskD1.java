package on2019_11.on2019_11_24_Codeforces_Round__602__Div__1__based_on_Technocup_2020_Elimination_Round_3_.D1___Wrong_Answer_on_test_233__Easy_Version_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Modular;
import template.math.Power;

public class TaskD1 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = in.readInt();
        }
        if (k == 1 || n == 1) {
            out.println(0);
            return;
        }

        int special = 0;
        for (int i = 0; i < n; i++) {
            if (h[i] != h[(i + 1) % n]) {
                special++;
            }
        }

        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        Composite comp = new Composite(Math.max(1, special), mod);
        int equal = 0;
        for (int i = 0; i + i <= special; i++) {
            int local = comp.composite(special, i);
            local = mod.mul(local, comp.composite(special - i, i));
            local = mod.mul(local, pow.pow(k - 2, special - i - i));
            equal = mod.plus(equal, local);
        }

        int total = pow.pow(k, special);
        total = mod.subtract(total, equal);
        total = mod.mul(total, pow.inverse(2));
        total = mod.mul(total, pow.pow(k, n - special));
        out.println(total);
    }
}
