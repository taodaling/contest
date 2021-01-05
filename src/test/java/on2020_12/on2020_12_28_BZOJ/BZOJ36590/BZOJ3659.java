package on2020_12.on2020_12_28_BZOJ.BZOJ36590;




import template.graph.MatrixTreeTheorem;
import template.graph.MatrixTreeTheoremBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class BZOJ3659 {
    int mod = 1000003;
    Factorial fact = new Factorial(200000, mod);

    IntegerArrayList a = new IntegerArrayList();
    IntegerArrayList b = new IntegerArrayList();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 0) {
            throw new UnknownError();
        }
        MatrixTreeTheoremBeta mtt = new MatrixTreeTheoremBeta(n, mod, true);
        a.clear();
        b.clear();
        long prod = 1;
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            if (i == 0) {
                prod = k;
            }
            for (int j = 0; j < k; j++) {
                int to = in.ri() - 1;
                mtt.addDirectedEdge(i, to, 1);
                a.add(i);
                b.add(to);
            }
        }
        if (n == 1) {
            out.println(1);
            return;
        }
        int ans = mtt.countEulerTrace(fact);
        int ans2 = MatrixTreeTheorem.countDirectedGraphEulerTour(n, a.toArray(), b.toArray(), mod, true);
        Debug debug = new Debug(true);
        debug.debug("ans", ans);
        debug.debug("ans2", ans2);
        out.println(ans * prod % mod);
    }
}
