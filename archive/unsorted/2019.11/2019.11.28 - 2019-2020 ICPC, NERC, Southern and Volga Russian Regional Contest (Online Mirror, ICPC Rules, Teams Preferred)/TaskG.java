package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt();
        }
        int[] dpLast = new int[n];
        int[] dp = new int[n];
        int[] winReq = new int[n];
        int[] winLast = new int[n];
        SequenceUtils.deepFill(dpLast, -1);
        SequenceUtils.deepFill(dp, (int) 1e8);
        SequenceUtils.deepFill(winReq, (int) 1e8);
        SequenceUtils.deepFill(winLast, -1);

        Segment seg = new Segment(0, n);
        Recorder recorder = new Recorder();

        long preA = 0;
        long preB = 0;
        TreeMap<Long, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            preA += a[i];
            preB += b[i];
            //win
            if (preA < preB && preB >= k) {
                if (preA < k) {
                    winReq[i] = 0;
                } else {
                    int atLeast = ceilIndex(map, preA - k + 1);
                    int noMoreThan = floorIndex(map, preB - k);
                    recorder.init();
                    seg.query(atLeast, noMoreThan, 0, n, recorder);
                    winReq[i] = recorder.val + 1;
                    winLast[i] = recorder.index;
                }
            }
            //dp
            if (preA >= k) {
                int atLeast = ceilIndex(map, preA - k + 1);
                recorder.init();
                seg.query(atLeast, n, 0, n, recorder);
                dp[i] = recorder.val + 1;
                dpLast[i] = recorder.index;
            } else {
                dp[i] = 0;
            }
            seg.update(i, i, 0, n, dp[i], i);
            //treemap
            map.put(Math.min(preA, preB), i);
        }

        int minWinCost = CompareUtils.minOf(winReq, 0, n - 1);
        if (minWinCost >= n) {
            out.println(-1);
            return;
        }
        int index = winLast[SequenceUtils.indexOf(winReq, 0, n - 1, minWinCost)];
        IntegerList chain = new IntegerList(n);
        while (index != -1) {
            chain.add(index);
            index = dpLast[index];
        }
        out.println(minWinCost);
        for (int i = chain.size() - 1; i >= 0; i--) {
            out.append(chain.get(i) + 1).append(' ');
        }
        out.println();
    }

    public int floorIndex(TreeMap<Long, Integer> map, Long key) {
        Map.Entry<Long, Integer> e = map.floorEntry(key);
        return e == null ? (int) -1 : e.getValue();
    }

    public int ceilIndex(TreeMap<Long, Integer> map, Long key) {
        Map.Entry<Long, Integer> e = map.ceilingEntry(key);
        return e == null ? (int) 1e8 : e.getValue();
    }
}

class Recorder {
    int val;
    int index;

    public void init() {
        val = (int) 1e8;
        index = -1;
    }

    public void update(int val, int index) {
        if (this.val > val) {
            this.val = val;
            this.index = index;
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int min = (int) 1e8;
    private int index = -1;

    public void pushUp() {
        if (left.min < right.min) {
            min = left.min;
            index = left.index;
        } else {
            min = right.min;
            index = right.index;
        }
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x, int index) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (min > x) {
                min = x;
                this.index = index;
            }
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x, index);
        right.update(ll, rr, m + 1, r, x, index);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, Recorder recorder) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            recorder.update(min, index);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, recorder);
        right.query(ll, rr, m + 1, r, recorder);
    }
}


