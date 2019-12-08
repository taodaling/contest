package on2019_12.on2019_12_07_Codeforces_Round__539__Div__1_.D___Sasha_and_Interesting_Fact_from_Graph_Theory;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class TaskD {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(1000000, mod);
    Factorial factorial = new Factorial(1000000, mod);
    Composite comp = new Composite(factorial);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        CachedPow cachedPow = new CachedPow(m, n, mod);
        int ans = 0;
        for (int i = 0; i <= n - 2; i++) {
            int local = comp.composite(n - 2, i);
            local = mod.mul(local, factorial.fact(i));
            int total = i + 1;
            local = mod.mul(local, comp.composite((m - total) + total - 1, total - 1));
            local = mod.mul(local, cachedPow.pow(n - 1 - total));
            local = mod.mul(local, countTree(n - 2 - i + 1, 2 + i));
            ans = mod.plus(ans, local);
        }

        out.println(ans);
    }

    public int countTree(int n, int x) {
        if (n == 1) {
            return 1;
        }
        int p1 = x;
        int p2 = power.pow(n - 1, n - 2);
        int p3 = mod.plus(1, mod.mul(x, inverseNumber.inverse(n - 1)));
        p3 = power.pow(p3, n - 2);
        return mod.mul(p1, mod.mul(p2, p3));
    }
}
