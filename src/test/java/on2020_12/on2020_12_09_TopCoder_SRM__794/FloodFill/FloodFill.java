package on2020_12.on2020_12_09_TopCoder_SRM__794.FloodFill;



import template.algo.IntBinarySearch;
import template.datastructure.ActiveSegment;
import template.datastructure.Range2DequeAdapter;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIntervalMap;
import template.problem.RectUnionArea;
import template.problem.RectUnionArea.Rect;

import java.util.*;

public class FloodFill {
   // Debug debug = new Debug(false);

    public int[] getCell(int[] X, int[] Y, long A) {
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                int time = (int)1e9;
//                for(int t = 0; t < X.length; t++){
//                    time = Math.min(Math.max(Math.abs(X[t] - i),
//                            Math.abs(Y[t] - j)), time);
//                }
//                if(time == 2){
//                    System.err.append('*');
//                }else{
//                    System.err.append('.');
//                }
//            }
//            System.err.println();
//        }

        int n = X.length;
        long finalA = A;
        IntBinarySearch bs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                Rect[] rects = new Rect[n];
                for (int i = 0; i < n; i++) {
                    rects[i] = new Rect();
                    rects[i].l = X[i] - mid;
                    rects[i].r = X[i] + mid;
                    rects[i].b = Y[i] - mid;
                    rects[i].t = Y[i] + mid;
                }
                return RectUnionArea.unionArea(rects) - n >= finalA;
            }
        };
        int time = bs.binarySearch(0, (int) 2e8);
        //debug.debug("time", time);
        List<Rect> deleted = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Rect r = new Rect();
            r.l = X[i] - time + 1;
            r.r = X[i] + time - 1;
            r.b = Y[i] - time + 1;
            r.t = Y[i] + time - 1;
            deleted.add(r);
        }
        A -= RectUnionArea.unionArea(deleted.toArray(new Rect[0])) - n;
        Map<Integer, IntegerIntervalMap> cols = new HashMap<>();
        Map<Integer, IntegerIntervalMap> rows = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int l = X[i] - time;
            int r = X[i] + time;
            int b = Y[i] - time;
            int t = Y[i] + time;
            cols.computeIfAbsent(l, x -> new IntegerIntervalMap()).add(b, t + 1);
            cols.computeIfAbsent(r, x -> new IntegerIntervalMap()).add(b, t + 1);
            rows.computeIfAbsent(b, x -> new IntegerIntervalMap()).add(l, r + 1);
            rows.computeIfAbsent(t, x -> new IntegerIntervalMap()).add(l, r + 1);
        }
        for (Map.Entry<Integer, IntegerIntervalMap> col : cols.entrySet()) {
            int x = col.getKey();
            IntegerIntervalMap colMap = col.getValue();
            for (Rect d : deleted) {
                if (d.l <= x && d.r >= x) {
                    colMap.remove((int)d.b, (int)d.t + 1);
                }
            }
            for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
                int y = row.getKey();
                IntegerIntervalMap rowMap = row.getValue();
                if (colMap.contain(y, y + 1)) {
                    rowMap.remove(x, x + 1);
                }
            }
        }
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            for (Rect d : deleted) {
                if (d.b <= y && d.t >= y) {
                    map.remove((int)d.l, (int)d.r + 1);
                }
            }
        }

        CountSegment cs = new CountSegment();
        int lower = (int) -1e9;
        int upper = (int) 1e9;
        for (Map.Entry<Integer, IntegerIntervalMap> col : cols.entrySet()) {
            int x = col.getKey();
            IntegerIntervalMap colMap = col.getValue();
            cs.update(x, x, lower, upper, colMap.total());
        }
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            for (IntegerIntervalMap.Interval interval : map) {
                cs.update(interval.l, interval.r - 1, lower, upper, 1);
            }
        }
        int x = cs.kth(lower, upper, A);
        IntegerIntervalMap now = cols.computeIfAbsent(x, t -> new IntegerIntervalMap());
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            if (map.contain(x, x + 1)) {
                now.add(y, y + 1);
            }
        }

        int y = -1;
        A -= cs.kth(lower, x - 1, lower, upper);
        for (IntegerIntervalMap.Interval interval : now) {
            if (interval.length() < A) {
                A -= interval.length();
                continue;
            }
            y = (int) (interval.l + A - 1);
            break;
        }

        return new int[]{x, y};
    }

}


class CountSegment implements Cloneable {
    private static final CountSegment NIL = new CountSegment();
    private CountSegment left = NIL;
    private CountSegment right = NIL;
    private long sum;
    private long dirty;

    static {
        NIL.left = NIL.right = NIL;
    }

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public void pushDown(int l, int r) {
        if (left == NIL) {
            left = left.clone();
            right = right.clone();
        }
        if (dirty != 0) {
            int m = DigitUtils.floorAverage(l, r);
            left.modify(l, m, dirty);
            right.modify(m + 1, r, dirty);
            dirty = 0;
        }
    }

    public void modify(int l, int r, long d) {
        assert this != NIL;
        dirty += d;
        sum += (r - l + 1) * d;
    }

    public CountSegment() {
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int mod) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(l, r, mod);
            return;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, mod);
        right.update(ll, rr, m + 1, r, mod);
        pushUp();
    }


    public int kth(int l, int r, long k) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown(l, r);
        if (left.sum >= k) {
            return left.kth(l, m, k);
        }
        return right.kth(m + 1, r, k - left.sum);
    }

    public long kth(int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r)) {
            return 0;
        }
        if (enter(ll, rr, l, r)) {
            return sum;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        return left.kth(ll, rr, l, m) +
                right.kth(ll, rr, m + 1, r);
    }

    public CountSegment merge(int l, int r, CountSegment segment) {
        if (this == NIL) {
            return segment;
        } else if (segment == NIL) {
            return this;
        }
        if (l == r) {
            sum += segment.sum;
        }
        int m = DigitUtils.floorAverage(l, r);
        left = merge(l, m, segment.left);
        right = merge(m + 1, r, segment.right);
        pushUp();
        return this;
    }

    @Override
    public CountSegment clone() {
        try {
            return (CountSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}