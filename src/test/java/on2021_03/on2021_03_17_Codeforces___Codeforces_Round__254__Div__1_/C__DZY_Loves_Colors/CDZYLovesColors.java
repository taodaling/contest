package on2021_03.on2021_03_17_Codeforces___Codeforces_Round__254__Div__1_.C__DZY_Loves_Colors;


import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongBITExt;
import template.utils.CloneSupportObject;
import template.utils.CollectionUtils;

import java.util.TreeMap;

public class CDZYLovesColors {
    private void split(TreeMap<Integer, Interval> map, int pt) {
        Interval floor = CollectionUtils.floorValue(map, pt);
        if (floor != null && floor.r >= pt && floor.l < pt) {
            Interval clone = floor.clone();
            clone.l = pt;
            floor.r = pt - 1;
            map.put(clone.l, clone);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        LongBITExt bit = new LongBITExt(n);
        TreeMap<Integer, Interval> map = new TreeMap<>();
        for (int i = 1; i <= n; i++) {
            map.put(i, new Interval(i, i, i));
        }
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int l = in.ri();
            int r = in.ri();
            if (t == 1) {
                int x = in.ri();
                split(map, l);
                split(map, r + 1);
                Interval newInterval = new Interval(l, r, x);
                while (true) {
                    Interval ceil = CollectionUtils.ceilValue(map, l);
                    if (ceil == null || ceil.l > r) {
                        break;
                    }
                    map.remove(ceil.l);
                    int delta = Math.abs(x - ceil.x);
                    bit.update(ceil.l, ceil.r, delta);
                }
                map.put(newInterval.l, newInterval);
            }else{
                long ans = bit.query(l, r);
                out.println(ans);
            }
        }
    }
}

class Interval extends CloneSupportObject<Interval> {
    int l;
    int r;
    int x;

    public Interval(int l, int r, int x) {
        this.l = l;
        this.r = r;
        this.x = x;
    }
}