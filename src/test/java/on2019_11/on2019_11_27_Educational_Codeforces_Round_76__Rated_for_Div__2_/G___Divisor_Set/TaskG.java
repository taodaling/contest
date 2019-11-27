package on2019_11.on2019_11_27_Educational_Codeforces_Round_76__Rated_for_Div__2_.G___Divisor_Set;



import template.datastructure.IntHashMap;
import template.datastructure.IntList;
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
        List<IntList> polynomials = new ArrayList<>();
        for (int i = 0; i <= 3000000; i++) {
            if (cnts[i] == 0) {
                continue;
            }
            IntList list = new IntList(cnts[i] + 1);
            list.expandWith(1, cnts[i] + 1);
            polynomials.add(list);
        }

        Modular mod = new Modular(998244353);
        IntList ans = new IntList();
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.dacMul(polynomials.toArray(new IntList[0]), ans);
        out.println(ans.get(n / 2));
    }
}
