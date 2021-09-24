package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P5___Crazy_Cyclic_Coincidences;



import template.datastructure.IntLinearBasis;
import template.datastructure.LinearBasis;
import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;

public class P5CrazyCyclicCoincidences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int v = in.ri();
        IntLinearBasis basis = new IntLinearBasis();
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int l = in.ri();
            if (dsu.find(a) == dsu.find(b)) {
                basis.add(l ^ dsu.delta(a, b));
            } else {
                dsu.merge(a, b, l);
            }
        }
        out.println(basis.contain(v) ? "yes" : "no");
    }
}
