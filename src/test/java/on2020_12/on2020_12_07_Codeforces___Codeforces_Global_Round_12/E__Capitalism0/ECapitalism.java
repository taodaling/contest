package on2020_12.on2020_12_07_Codeforces___Codeforces_Global_Round_12.E__Capitalism0;




import template.datastructure.XorDeltaDSU;
import template.graph.DifferSystem;
import template.io.FastInput;
import template.io.FastOutput;

public class ECapitalism {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.init();
        DifferSystem dcs = new DifferSystem(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            if (dsu.find(a) == dsu.find(b) && dsu.delta(a, b) == 0) {
                out.println("NO");
                return;
            }
            dsu.merge(a, b, 1);
            if (c == 1) {
                dcs.equalTo(a, b, -1);
            } else {
                dcs.lessThanOrEqualTo(a, b, 1);
                dcs.lessThanOrEqualTo(b, a, 1);
            }
        }
        int pairA = -1;
        long dist = -(int) 1e9;
        for (int i = 0; i < n; i++) {
            if (dcs.runSpfaSince(i)) {
                long max = 0;
                for (int j = 0; j < n; j++) {
                    max = Math.max(dcs.possibleSolutionOf(j), max);
                }
                if (max > dist) {
                    dist = max;
                    pairA = i;
                }
            } else {
                out.println("NO");
                return;
            }
        }

        out.println("YES");
        out.println(dist);
        dcs.runSpfaSince(pairA);
        for (int i = 0; i < n; i++) {
            out.append(dcs.possibleSolutionOf(i)).append(' ');
        }
    }


}
