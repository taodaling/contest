package on2020_04.on2020_04_13_TopCoder_SRM__760.HomeAwayLeague;



import template.math.Composite;
import template.math.Factorial;
import template.math.Modular;

public class HomeAwayLeague {
    public int matches(int n) {
        int m = n / 2;
        Modular mod = new Modular(1e9 + 7);
        Factorial factorial = new Factorial((int) 1e6, mod);
        Composite comp = new Composite(factorial);

        int a = mod.mul(comp.composite(n, m), factorial.fact(m));
        int b = 0;
        for (int i = 0; i <= m; i++) {
            int sign = mod.valueOf(i % 2 == 0 ? 1 : -1);
            int local = mod.mul(sign, factorial.fact(m - i));
            local = mod.mul(local, comp.composite(m, i));
            b = mod.plus(b, local);
        }
        int ans = mod.mul(a, b);
        return ans;
    }
}
