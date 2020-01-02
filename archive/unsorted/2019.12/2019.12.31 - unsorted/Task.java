package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.problem.WayToSplitNumber;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int mod = in.readInt();
        Modular modular = new Modular(mod);
        WayToSplitNumber wayToSplitNumber = null;
        if (mod == 998244353) {
            wayToSplitNumber = new WayToSplitNumber(n, modular, new NumberTheoryTransform(modular));
        } else {
            wayToSplitNumber = new WayToSplitNumber(n, modular);
        }

        for (int i = 0; i <= n; i++) {
            out.println(wayToSplitNumber.wayOf(i));
        }
    }
}
