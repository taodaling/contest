package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.HashSet;
import java.util.Set;

public class APeacefulRooks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int ans = 0;
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int x = in.ri() - 1;
            int y = in.ri() - 1;
            if (x == y) {
                continue;
            }
            ans++;
            if (dsu.find(x) == dsu.find(y)) {
                ans++;
            } else {
                dsu.merge(x, y);
            }
        }
        out.println(ans);
    }
}
