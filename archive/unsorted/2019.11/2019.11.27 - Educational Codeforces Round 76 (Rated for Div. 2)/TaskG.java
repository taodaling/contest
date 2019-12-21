package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;

import java.util.ArrayList;
import java.util.List;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] cnts = new int[3000000 + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }
        List<IntegerList> polynomials = new ArrayList<>();
        for (int i = 0; i <= 3000000; i++) {
            if (cnts[i] == 0) {
                continue;
            }
            IntegerList list = new IntegerList(cnts[i] + 1);
            list.expandWith(1, cnts[i] + 1);
            polynomials.add(list);
        }

        Modular mod = new Modular(998244353);
        IntegerList ans = new IntegerList();
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.dacMul(polynomials.toArray(new IntegerList[0]), ans);
        out.println(ans.get(n / 2));
    }
}
