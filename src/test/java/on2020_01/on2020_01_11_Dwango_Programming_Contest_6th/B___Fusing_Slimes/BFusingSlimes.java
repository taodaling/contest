package on2020_01.on2020_01_11_Dwango_Programming_Contest_6th.B___Fusing_Slimes;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.math.Modular;
import template.math.Power;

import java.lang.reflect.Array;
import java.util.Arrays;

public class BFusingSlimes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.readInt();
        }
//
//        Modular mod = new Modular(1e9 + 7);
//        Power power = new Power(mod);
//        Factorial fact = new Factorial(n, mod);
//        int inv2 = power.inverseByFermat(2);
//
//        int[] exp = new int[n];
//        exp[n - 1] = 0;
//        exp[n - 2] = x[n - 1] - x[n - 2];
//        for (int i = n - 3; i >= 0; i--) {
//            int contrib = 0;
//            contrib = mod.mul(inv2, exp[i + 1]);
//            contrib = mod.plus(contrib, x[i + 1] - x[i]);
//            exp[i] = contrib;
//        }
//
//        System.err.println(Arrays.toString(exp));
//        int ans = 0;
//        for (int i = 0; i < n; i++) {
//            ans = mod.plus(ans, exp[i]);
//        }
//        ans = mod.mul(ans, fact.fact(n - 1));

        out.println(bf(x));
    }

    public int bf(int[] x) {
        int n = x.length;
        int[] exp = new int[n];

        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        Factorial fact = new Factorial(n, mod);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int contrib = x[j] - x[i];
                int cnt = j - i + 1;
                int prob = 0;
                if (j < n - 1) {
                    prob = mod.mul(fact.fact(cnt - 2), fact.invFact(cnt));
                } else {
                    prob = mod.mul(fact.fact(cnt - 2), fact.invFact(cnt - 1));
                }
                exp[i] = mod.plus(exp[i], mod.mul(contrib, prob));
            }
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = mod.plus(ans, exp[i]);
        }

        return mod.mul(ans, fact.fact(n - 1));
    }
}
