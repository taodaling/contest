package on2019_12.on2019_12_13_Educational_Codeforces_Round_57__Rated_for_Div__2_.G___Lucky_Tickets;



import numeric.NTT;
import template.datastructure.IntList;
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
        IntList p = new IntList();
        p.addAll(allow);

        int m = n / 2;
        IntList last = p.clone();
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 3);
        List<IntList> lists = new ArrayList<>();
        while (m > 1) {
            if (m % 2 == 1) {
                lists.add(last.clone());
            }
            ntt.pow2(last);
            Polynomials.normalize(last);
            m /= 2;
        }
        lists.add(last);

        IntList prod = new IntList();
        ntt.mulByPQ(lists.toArray(new IntList[0]), prod);

        int ans = 0;
        for (int i = 0; i < prod.size(); i++) {
            int plus = prod.get(i);
            ans = mod.plus(ans, mod.mul(plus, plus));
        }

        out.println(ans);
    }
}
