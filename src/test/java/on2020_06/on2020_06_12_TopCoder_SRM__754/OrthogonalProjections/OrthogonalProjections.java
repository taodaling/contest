package on2020_06.on2020_06_12_TopCoder_SRM__754.OrthogonalProjections;



import template.geometry.geo2.IntegerPoint2;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class OrthogonalProjections {
    Debug debug = new Debug(true);

    public int[] generate(int n) {
        int[] ans = generate0(n);
        //debug.debug("ans.length", ans.length);
        //check(ans, n);
        return ans;
    }

    public void check(int[] pts, int n) {
        debug.debug("pts", pts);
        int m = pts.length / 2;
        if (m > 500) {
            throw new RuntimeException();
        }
        Set<IntegerPoint2> set = new TreeSet<>(IntegerPoint2.SORT_BY_POLAR_ANGLE);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                int dx = pts[i * 2 + 0] - pts[j * 2 + 0];
                int dy = pts[i * 2 + 1] - pts[j * 2 + 1];
                set.add(new IntegerPoint2(dx, dy));
                set.add(new IntegerPoint2(-dx, -dy));
            }
        }

        if (set.size() != n) {
            throw new RuntimeException();
        }
    }

    public int[] generate0(int n) {
        List<int[]> bot = new ArrayList<>();
        List<int[]> top = new ArrayList<>();
        if (n == 1) {
            return new int[]{0, 0};
        }
        if (n % 2 == 1) {
            return new int[0];
        }
        if (n == 2) {
            return new int[]{0, 0, 1, 1};
        }

        int m = n / 2;
        int k = Math.min(m, 250);
        int b;
        int a;
        while (true) {
            if (k == 0) {
                return new int[0];
            }
            b = m / k - 1;
            a = m - (b + 1) * k;
            if (a == 0 || a + b + k > 500) {
                k--;
                continue;
            }
            break;
        }

        int unit = 1000;

        for (int i = 0; i < k; i++) {
            bot.add(new int[]{i * unit, 0});
        }
        for (int i = 0; i < a; i++) {
            top.add(new int[]{i * unit, 1});
        }

        for (int i = 1; i <= b; i++) {
            top.add(new int[]{i, 1});
        }

        List<int[]> pts = new ArrayList<>();

        pts.addAll(bot);
        pts.addAll(top);
        int[] res = new int[pts.size() * 2];
        for (int i = 0; i < pts.size(); i++) {
            int[] pt = pts.get(i);
            res[i * 2 + 0] = pt[0];
            res[i * 2 + 1] = pt[1];
        }
        return res;
    }
}
