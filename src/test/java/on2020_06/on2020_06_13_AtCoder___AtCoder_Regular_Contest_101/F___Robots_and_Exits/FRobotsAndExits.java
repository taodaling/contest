package on2020_06.on2020_06_13_AtCoder___AtCoder_Regular_Contest_101.F___Robots_and_Exits;



import com.sun.org.apache.xpath.internal.operations.Mod;
import template.datastructure.DiscreteMap;
import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.Randomized;
import template.utils.Debug;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FRobotsAndExits {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] x = new int[n];
        int[] y = new int[m];
        in.populate(x);
        in.populate(y);


        List<int[]> pts = new ArrayList<>(n);
        IntegerList dy = new IntegerList(n);
        int l = 0;
        for (int i = 0; i < n; i++) {
            if (x[i] < y[l]) {
                continue;
            }
            while (l + 1 < m && y[l + 1] <= x[i]) {
                l++;
            }
            if (l == m - 1) {
                continue;
            }
            if (x[i] == y[l] || y[l + 1] == x[i]) {
                continue;
            }
            pts.add(new int[]{x[i] - y[l], y[l + 1] - x[i]});
        }

        for (int[] pt : pts) {
            dy.add(pt[1]);
        }
        IntegerDiscreteMap dmY = new IntegerDiscreteMap(dy.getData(), 0, dy.size());
        for (int[] pt : pts) {
            pt[1] = dmY.rankOf(pt[1]) + 2;
        }

        for (int[] pt : pts) {
            debug.debug("pt", pt);
        }

        pts.sort((a, b) -> a[0] == b[0] ? -Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]));
        Modular mod = new Modular(1e9 + 7);
        ModBIT bit = new ModBIT(pts.size() + 1, mod);
        bit.update(1, 1);

        for (int i = 0; i < pts.size(); i++) {
            int ptX = pts.get(i)[0];
            int r = i - 1;
            int lastY = -1;
            while (r + 1 < pts.size() && pts.get(r + 1)[0] == ptX) {
                r++;
                int[] pt = pts.get(r);
                if (pt[1] == lastY) {
                    continue;
                }
                lastY = pt[1];
                int sum = bit.query(pt[1] - 1);
                bit.update(pt[1], sum);
            }
            i = r;
        }

        int ans = bit.query(pts.size() + 1);
        out.println(ans);
    }
}
