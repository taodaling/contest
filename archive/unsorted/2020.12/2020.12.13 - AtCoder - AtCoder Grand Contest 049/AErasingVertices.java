package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;


public class AErasingVertices {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        BitSet[] from = new BitSet[n];
        for (int i = 0; i < n; i++) {
            from[i] = new BitSet(100);
            from[i].set(i);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (in.rc() == '1') {
                    from[j].set(i);
                }
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (from[i].get(k) && from[k].get(j)) {
                        from[i].set(j);
                    }
                }
            }
        }

        double exp = 0;
        for (int i = 0; i < n; i++) {
            exp += 1.0 / from[i].size();
        }
        out.println(exp);
    }

}
