package contest;

import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class CNationalProperty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        s = new int[n][];
        for (int i = 0; i < n; i++) {
            int len = in.readInt();
            s[i] = new int[len + 1];
            for (int j = 0; j < len; j++) {
                s[i][j] = in.readInt() - 1;
            }
            s[i][len] = -1;
        }

        ts = new TwoSatBeta(m, 300000);
        order(0, n - 1, 0);

        valid = valid && ts.solve(true);
        if (!valid) {
            out.println("No");
            return;
        }
        out.println("Yes");
        IntegerList capital = new IntegerList(m);
        for (int i = 0; i < m; i++) {
            if (ts.valueOf(i)) {
                capital.add(i);
            }
        }
        out.println(capital.size());
        for (int i = 0; i < capital.size(); i++) {
            out.append(capital.get(i) + 1).append(' ');
        }
    }

    TwoSatBeta ts;
    int[][] s;

    boolean valid = true;

    public void order(int l, int r, int index) {
        if (l >= r) {
            return;
        }

        for (int i = l; i <= r && valid; i++) {
            int j = i;
            int v = s[j][index];
            while (j + 1 <= r && s[j + 1][index] == v) {
                j++;
            }
            if (v != -1) {
                order(i, j, index + 1);
            }

            i = j;
            if (j + 1 <= r) {
                int next = s[j + 1][index];
                if (next == -1) {
                    valid = false;
                    continue;
                }
                if (v == -1) {
                    continue;
                }
                if (v < next) {
                    ts.deduce(ts.elementId(next), ts.elementId(v));
                } else {
                    ts.isTrue(ts.elementId(v));
                    ts.isFalse(ts.elementId(next));
                }
            }
        }
    }
}
