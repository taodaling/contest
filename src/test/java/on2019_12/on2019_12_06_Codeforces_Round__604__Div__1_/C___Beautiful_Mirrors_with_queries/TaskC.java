package on2019_12.on2019_12_06_Codeforces_Round__604__Div__1_.C___Beautiful_Mirrors_with_queries;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Log2;
import template.math.Modular;
import template.math.Power;

import java.util.Map;
import java.util.TreeMap;

public class TaskC {
    Modular mod = new Modular(998244353);
    Power pow = new Power(mod);
    Interval[][] intervals;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] p = new int[n];
        int inv100 = pow.inverse(100);
        for (int i = 0; i < n; i++) {
            p[i] = mod.mul(in.readInt(), inv100);
        }
        int[] preMul = new int[n];
        int[] preInv = new int[n];
        for (int i = 0; i < n; i++) {
            preMul[i] = p[i];
            if (i > 0) {
                preMul[i] = mod.mul(preMul[i - 1], preMul[i]);
            }
            preInv[i] = pow.inverse(preMul[i]);
        }

        TreeMap<Integer, Integer> set = new TreeMap<>();


        intervals = new Interval[n + 1][20];
        intervals[n][0] = new Interval();
        for (int i = n - 1; i >= 0; i--) {
            intervals[i][0] = new Interval();
            intervals[i][0].x = 1;
            intervals[i][0].y = mod.subtract(1, p[i]);
            intervals[i][0].k = p[i];
            for (int j = 0; (i + (1 << j)) <= n && intervals[i + (1 << j)][j] != null; j++) {
                intervals[i][j + 1] = merge(intervals[i][j], intervals[i + (1 << j)][j]);
            }
        }

        int totalExp = answer(query(0, n - 1));
        set.put(0, totalExp);
        for (int i = 0; i < q; i++) {
            int point = in.readInt() - 1;
            int floor = set.floorKey(point);
            if (floor == point) {
                //remove
                totalExp = mod.subtract(totalExp, set.remove(point));
                Map.Entry<Integer, Integer> floorEntry = set.floorEntry(point);
                totalExp = mod.subtract(totalExp, floorEntry.getValue());
                Integer right = set.ceilingKey(point);
                if (right == null) {
                    right = n;
                }
                int modify = answer(query(floorEntry.getKey(), right - 1));
                totalExp = mod.plus(totalExp, modify);
                set.put(floorEntry.getKey(), modify);
            } else {
                //add
                Map.Entry<Integer, Integer> floorEntry = set.floorEntry(point);
                totalExp = mod.subtract(totalExp, floorEntry.getValue());
                int floorEntryNewValue = answer(query(floorEntry.getKey(), point - 1));
                totalExp = mod.plus(totalExp, floorEntryNewValue);
                set.put(floorEntry.getKey(), floorEntryNewValue);

                Integer right = set.ceilingKey(point);
                if (right == null) {
                    right = n;
                }
                int modify = answer(query(point, right - 1));
                totalExp = mod.plus(totalExp, modify);
                set.put(point, modify);
            }
            out.println(totalExp);
        }
    }

    Log2 log2 = new Log2();

    public int answer(Interval interval) {
        return mod.mul(interval.x, pow.inverse(mod.subtract(1, interval.y)));
    }

    public Interval query(int l, int r) {
        int floorLog = log2.floorLog(r - l + 1);
        Interval left = intervals[l][floorLog];
        l += 1 << floorLog;
        if (l <= r) {
            left = merge(left, query(l, r));
        }
        return left;
    }

    public Interval merge(Interval l, Interval r) {
        Interval ans = new Interval();
        ans.x = mod.plus(l.x, mod.mul(r.x, l.k));
        ans.y = mod.plus(l.y, mod.mul(r.y, l.k));
        ans.k = mod.mul(l.k, r.k);
        return ans;
    }
}

class Interval {
    int x;
    int y;
    int k;
}