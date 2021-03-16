package contest;

import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;

public class BTwoSets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int[] p = in.ri(n);
        IntegerHashMap map = new IntegerHashMap(n, false);
        for (int i = 0; i < n; i++) {
            map.put(p[i], i);
        }
        TwoSatBeta ts = new TwoSatBeta(n, 4 * n);
        for (int i = 0; i < n; i++) {
            if (map.containKey(a - p[i])) {
                ts.deduce(ts.elementId(i), ts.elementId(map.get(a - p[i])));
            } else {
                ts.isFalse(ts.elementId(i));
            }
            if (map.containKey(b - p[i])) {
                ts.deduce(ts.negateElementId(i), ts.negateElementId(map.get(b - p[i])));
            } else {
                ts.isFalse(ts.negateElementId(i));
            }
        }
        boolean possible = ts.solve(true);
        if (!possible) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < n; i++) {
            out.append(ts.valueOf(i) ? 0 : 1).append(' ');
        }
    }
}
