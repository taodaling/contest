package on2021_05.on2021_05_07_Codeforces___Codeforces_Round__189__Div__1_.E__Ping_Pong;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.*;

public class EPingPong {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        DSUExt ext = new DSUExt(n);
        ext.init();
        int id = 0;
        List<Interval> intersect = new ArrayList<>(n);
        List<Interval> contain = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int t = in.ri();
            if (t == 2) {
                int x = in.ri() - 1;
                int y = in.ri() - 1;
                x = ext.find(x);
                y = ext.find(y);
                Interval ix = ext.intervals[x];
                Interval iy = ext.intervals[y];
                if (iy.l == ix.l && iy.r == ix.r) {
                    if (ext.maxId[x] <= ext.maxId[y]) {
                        out.println("YES");
                    } else {
                        out.println("NO");
                    }
                } else if (iy.l <= ix.l && iy.r >= ix.r) {
                    out.println("YES");
                } else {
                    out.println("NO");
                }
            } else {
                int cur = id;
                id++;
                int l = in.ri();
                int r = in.ri();
                Interval interval = new Interval(l, r, cur);
                ext.intervals[cur] = interval;
                intersect.clear();
                contain.clear();
                while (true) {
                    Interval visit = CollectionUtils.floorValue(ext.global, r - 1);
                    if (visit == null || visit.r <= l) {
                        break;
                    }
                    if (visit.l >= l && visit.r <= r) {
                        contain.add(visit);
                    } else {
                        intersect.add(visit);
                    }
                    ext.global.remove(visit.l);
                }
                for (Interval visit : contain) {
                    ext.sets[cur].put(visit.l, visit);
                }
                ext.global.put(interval.l, interval);
                for (Interval visit : intersect) {
                    ext.merge(cur, visit.id);
                }
                while (true) {
                    boolean updated = false;
                    int root = ext.find(cur);
                    {
                        Interval visit = CollectionUtils.floorValue(ext.sets[root], l - 1);
                        if (visit != null && visit.r > l) {
                            //merge
                            ext.sets[root].remove(visit.l);
                            ext.merge(visit.id, cur);
                            updated = true;
                        }
                    }
                    {
                        Interval visit = CollectionUtils.floorValue(ext.sets[root], r - 1);
                        if (visit != null && visit.r > r) {
                            //merge
                            ext.sets[root].remove(visit.l);
                            ext.merge(visit.id, cur);
                            updated = true;
                        }
                    }
                    if (!updated) {
                        break;
                    }
                }
            }
        }
    }
}

class DSUExt extends DSU {
    public DSUExt(int n) {
        super(n);
        sets = new TreeMap[n];
        for (int i = 0; i < n; i++) {
            sets[i] = new TreeMap<>();
        }
        intervals = new Interval[n];
        maxId = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            maxId[i] = i;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        maxId[a] = Math.max(maxId[a], maxId[b]);
        sets[a] = CollectionUtils.mergeMapHeuristically(sets[a], sets[b]);
        boolean eb = global.remove(intervals[b].l, intervals[b]);
        boolean ea = global.remove(intervals[a].l, intervals[a]);

        intervals[a].l = Math.min(intervals[a].l, intervals[b].l);
        intervals[a].r = Math.max(intervals[a].r, intervals[b].r);

        if (ea || eb) {
            global.put(intervals[a].l, intervals[a]);
        }
    }

    int[] maxId;
    Interval[] intervals;
    TreeMap<Integer, Interval>[] sets;
    TreeMap<Integer, Interval> global = new TreeMap<>();
}

class Interval {
    int l;
    int r;
    int id;

    public Interval(int l, int r, int id) {
        this.l = l;
        this.r = r;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "l=" + l +
                ", r=" + r +
                ", id=" + id +
                '}';
    }
}
