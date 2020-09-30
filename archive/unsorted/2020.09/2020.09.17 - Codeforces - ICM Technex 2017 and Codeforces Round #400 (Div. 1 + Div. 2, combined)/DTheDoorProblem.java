package contest;

import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class DTheDoorProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] status = new int[n];
        IntegerArrayList[] lists = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            status[i] = in.readInt();
            lists[i] = new IntegerArrayList(2);
        }
        for (int i = 0; i < m; i++) {
            int c = in.readInt();
            for (int j = 0; j < c; j++) {
                int x = in.readInt() - 1;
                lists[x].add(i);
            }
        }

        TwoSatBeta ts = new TwoSatBeta(m, 4 * m);
        for (int i = 0; i < n; i++) {
            int a = lists[i].get(0);
            int b = lists[i].get(1);
            if (status[i] == 0) {
                ts.xor(ts.elementId(a), ts.elementId(b));
            } else {
                ts.same(ts.elementId(a), ts.elementId(b));
            }
        }

        boolean ans = ts.solve(false);
        out.println(ans ? "YES" : "NO");
    }
}
