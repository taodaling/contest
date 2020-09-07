package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.QuadraticResidue;

public class SqrtMod {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int y = in.readInt();
        int p = in.readInt();
        QuadraticResidue qr = new QuadraticResidue(new Modular(p));
        int ans = qr.square(y);
        out.println(ans);
    }
}
