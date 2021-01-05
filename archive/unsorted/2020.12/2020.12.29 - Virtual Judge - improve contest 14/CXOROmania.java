package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.HashSet;

public class CXOROmania {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = 1;
        while (n * (n - 1) / 2 != m) {
            n++;
        }
        int[] b = new int[m];
        in.populate(b);
        if (n == 2) {
            out.append(0).append(' ').append(b[0]);
            return;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int x : b) {
            set.add(x);
        }
        boolean[][] edge = new boolean[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < i; j++) {
                if (set.contains(b[i] ^ b[j])) {
                    edge[i][j] = edge[j][i] = true;
                }
            }
        }

        IntegerArrayList elements = new IntegerArrayList(n);
        int a12 = 0;
        int a13 = -1;
        for (int i = 1; i < m; i++) {
            if (edge[0][i]) {
                a13 = i;
                break;
            }
        }
        elements.add(a12);
        elements.add(a13);
        for (int i = 0; i < m; i++) {
            if (i == a12 || i == a13) {
                continue;
            }
            if (edge[a12][i] && edge[a13][i] && (b[a13] ^ b[i]) != b[a12]) {
                elements.add(i);
            }
        }

        debug.debug("n", n);
        debug.debug("elements", elements);
        debug.debugMatrix("edge", edge);
        assert elements.size() == n - 1;
        out.append(0).append(' ');
        for (int x : elements.toArray()) {
            out.append(b[x]).append(' ');
        }
    }
}
