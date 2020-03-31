package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

public class EHeightAllTheSame {
    Modular mod = new Modular(998244353);
    Debug debug = new Debug(true);
    Power power = new Power(mod);

    public int odd(int n) {
        return (n + 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int l = in.readInt();
        int r = in.readInt();
        int odd = odd(r) - odd(l - 1);
        int even = (r - l + 1) - odd;
        debug.debug("odd", odd);
        debug.debug("even", even);

        if ((long) n * m % 2 == 1) {
            out.println(power.pow(r - l + 1, ((long) n * m)));
            return;
        }

        ModMatrix matrix = new ModMatrix(2, 2);
        matrix.set(0, 0, even);
        matrix.set(0, 1, odd);
        matrix.set(1, 0, odd);
        matrix.set(1, 1, even);
        matrix = ModMatrix.pow(matrix, (long) n * m, mod);
        ModMatrix vector = new ModMatrix(2, 1);
        vector.set(0, 0, 1);

        ModMatrix ans = ModMatrix.mul(matrix, vector, mod);
        out.println(ans.get(0, 0));
    }
}
