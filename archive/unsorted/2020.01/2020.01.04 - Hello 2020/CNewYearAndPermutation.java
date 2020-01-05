package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.math.Modular;

public class CNewYearAndPermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(m);
        Factorial fact = new Factorial(n, mod);

        int ans = 0;
        for(int i = 1; i <= n; i++){
            int p1 = n - i + 1;
            int p2 = n - i + 1;
            int p3 = fact.fact(i);
            int p4 = fact.fact(n - i);

            int local = mod.mul(p1, p2);
            local = mod.mul(local, p3);
            local = mod.mul(local, p4);

            ans = mod.plus(ans, local);
        }

        out.println(ans);
    }
}
