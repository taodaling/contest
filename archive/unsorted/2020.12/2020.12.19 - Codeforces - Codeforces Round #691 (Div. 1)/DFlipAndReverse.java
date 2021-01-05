package contest;

import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;

public class DFlipAndReverse {
    char[] s = new char[(int)1e6];
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s, 0);
        MultiSet<Integer> ms = new MultiSet<>();
        int ps = 0;
        for (int i = 0; i < n; i++) {
            int x = s[i] - '0';
            ps += x == 1 ? 1 : -1;
            if (x == 1) {
                ms.add(ps);
            }
        }
        ps = 0;
        for (int i = 0; i < n; i++) {
            int cnt = ms.count(ps);
            if (cnt > 0) {
                out.append(0);
                ps--;
            } else if (ms.count(ps + 1) > 0) {
                ms.remove(ps + 1);
                out.append(1);
                ps++;
            } else {
                out.append(0);
                ps--;
            }
        }
        out.println();
    }
}
