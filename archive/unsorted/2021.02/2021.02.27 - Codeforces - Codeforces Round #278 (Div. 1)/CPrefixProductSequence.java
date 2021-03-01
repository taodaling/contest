package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.MillerRabin;
import template.math.ModPrimeInverseNumber;
import template.math.Power;

public class CPrefixProductSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 4) {
            out.println("YES");
            out.println("1 3 2 4");
            return;
        }
        if (n == 1) {
            out.println("YES");
            out.println(1);
            return;
        }
        if (!MillerRabin.mr(n, 10)) {
            out.println("NO");
            return;
        }
        out.println("YES");
        out.append(1).append(' ');
        InverseNumber inv = new ModPrimeInverseNumber(n, n);
        for (int i = 2; i <= n - 1; i++) {
            out.append((long) i * inv.inverse(i - 1) % n).append(' ');
        }
        out.append(n);
    }
}
