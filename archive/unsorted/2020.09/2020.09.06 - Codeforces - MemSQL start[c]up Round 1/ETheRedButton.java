package contest;

import template.datastructure.DSU;
import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ETheRedButton {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n == 1) {
            out.println("0 0");
            return;
        }
        if (n % 2 == 1) {
            out.println("-1");
            return;
        }
        DSU dsu = new DSU(n);
        dsu.reset();
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            int match = (i * 2 + (i / (n / 2))) % n;
            next[i] = match;
            dsu.merge(i, next[i]);
        }
        for (int i = 0; i < n / 2; i++) {
            int a = i;
            int b = i + n / 2;
            if (dsu.find(a) != dsu.find(b)) {
                SequenceUtils.swap(next, a, b);
                dsu.merge(a, b);
            }
        }
        int cur = 0;
        while (true) {
            out.append(cur).append(' ');
            cur = next[cur];
            if (cur == 0) {
                break;
            }
        }
        out.println(cur);
    }
}
