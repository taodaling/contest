package contest;

import template.graph.DifferenceConstraintSystem;
import template.io.FastInput;
import template.io.FastOutput;

public class P1993K {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DifferenceConstraintSystem sys = new DifferenceConstraintSystem(n);
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (t == 1) {
                int c = in.ri();
                sys.differenceGreaterThanOrEqualTo(a, b, c);
            } else if (t == 2) {
                int c = in.ri();
                sys.differenceLessThanOrEqualTo(a, b, c);
            } else {
                sys.differenceEqualTo(a, b, 0);
            }
        }
        if(!sys.hasSolution()){
            out.println("No");
            return;
        }
        out.println("Yes");
    }
}
