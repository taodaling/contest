package contest;


import template.datastructure.XorDeltaDSU;
import template.graph.DifferenceConstraintSystem;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ECapitalism {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.init();
        DifferenceConstraintSystem dcs = new DifferenceConstraintSystem(n);
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
                dcs.differenceEqualTo(a, b, -1);
            } else {
                dcs.differenceLessThanOrEqualTo(a, b, 1);
                dcs.differenceLessThanOrEqualTo(b, a, 1);
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
