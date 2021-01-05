package contest;

import template.graph.MatrixTreeTheorem;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class BZOJ3659 {
    int mod = 1000003;

    IntegerArrayList a = new IntegerArrayList(200000);
    IntegerArrayList b = new IntegerArrayList(200000);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 0) {
            throw new UnknownError();
        }
        a.clear();
        b.clear();
        long prod = 1;
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            if(i == 0){
                prod = k;
            }
            for (int j = 0; j < k; j++) {
                a.add(i);
                b.add(in.ri() - 1);
            }
        }
        int ans = MatrixTreeTheorem.countDirectedGraphEulerTour(n, a.toArray(), b.toArray(), mod, false);
        out.println(ans * prod % mod);
    }
}
