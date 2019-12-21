package contest;

import numeric.NTT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedLog2;
import template.math.Factorial;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;

import java.util.ArrayList;
import java.util.List;

public class TaskG {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] allow = new int[10];
        for (int i = 0; i < k; i++) {
            allow[in.readInt()] = 1;
        }
        IntegerList p = new IntegerList();
        p.addAll(allow);

        int m = n / 2;
        IntegerList last = p.clone();
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 3);
        List<IntegerList> lists = new ArrayList<>();
        while (m > 1) {
            if (m % 2 == 1) {
                lists.add(last.clone());
            }
            ntt.pow2(last);
            Polynomials.normalize(last);
            m /= 2;
        }
        lists.add(last);

        IntegerList prod = new IntegerList();
        ntt.mulByPQ(lists.toArray(new IntegerList[0]), prod);

        int ans = 0;
        for (int i = 0; i < prod.size(); i++) {
            int plus = prod.get(i);
            ans = mod.plus(ans, mod.mul(plus, plus));
        }

        out.println(ans);
    }
}
