package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Modular;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int m = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        ModMatrix vec = new ModMatrix(m, 1);
        for (int i = 0; i < m; i++) {
            vec.set(i, 0, 1);
        }
        ModMatrix single = new ModMatrix(m, m);
        for (int i = 0; i < m - 1; i++) {
            single.set(i, i + 1, 1);
        }
        single.set(m - 1, 0, 1);
        single.set(m - 1, m - 1, 1);

        ModMatrix multi = ModMatrix.pow(single, n, mod);
        ModMatrix ans = ModMatrix.mul(multi, vec, mod);
        out.println(ans.get(0, 0));
    }
}
