package contest;

import template.FastInput;
import template.FastOutput;

public class TaskE {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long d = in.readInt();
        long[] a = new long[n + 1];
        long[] leftCost = new long[n + 1];
        long[] rightCost = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
            leftCost[i] = a[i] - i * d;
            rightCost[i] = a[i] + i * d;
        }

        Segment left = new Segment(1, n, leftCost);
        Segment right = new Segment(1, n, rightCost);

        left.update(1, 1, 1, n, -leftCost[1]);


        long ans = 0;
        for (int i = 1; i <= n; i++) {
            Recorder forLeft = new Recorder();
            Recorder forRight = new Recorder();
            left.query(1, n, 1, n, forLeft);
            right.query(1, n, 1, n, forRight);

            if (forLeft.val > forRight.val) {
                Recorder tmp = forLeft;
                forLeft = forRight;
                forRight = tmp;
            }

            ans += forLeft.val;
            left.kill(forLeft.index, forLeft.index, 1, n);
            right.kill(forLeft.index, forLeft.index, 1, n);
            left.update(1, forLeft.index - 1, 1, n, rightCost[forLeft.index]);
            right.update(forLeft.index + 1, n, 1, n, leftCost[forLeft.index]);
        }

        out.println(ans);
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long a;
    private long b = (long) 1e18;
    private long min = a + b;
    private int index;
    private int minAIndex;

    private void kill() {
        a = (long) 1e18;
        min = a + b;
    }

    public void updateB(long x) {
        b = Math.min(b, x);
        if (a + b < min) {
            min = a + b;
            if (left != null) {
                if (a == left.a) {
                    index = left.minAIndex;
                } else {
                    index = right.minAIndex;
                }
            }
        }
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        a = Math.min(left.a, right.a);
        if (min == left.min) {
            index = left.index;
        } else {
            index = right.index;
        }
        if(a == left.a){
            minAIndex = left.minAIndex;
        }else{
            minAIndex = right.minAIndex;
        }
    }

    public void pushDown() {
        left.updateB(b);
        right.updateB(b);
    }

    public Segment(int l, int r, long[] a) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, a);
            right = new Segment(m + 1, r, a);
            pushUp();
        } else {
            this.a = a[l];
            this.min = this.a + b;
            minAIndex = index = l;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            updateB(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void kill(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            kill();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.kill(ll, rr, l, m);
        right.kill(ll, rr, m + 1, r);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, Recorder ans) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            ans.add(min, index);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, ans);
        right.query(ll, rr, m + 1, r, ans);
    }
}


class Recorder {
    long val;
    int index;
    {
        prepare();
    }

    public void prepare() {
        val = (long) 1e18;
        index = -1;
    }

    public void add(long v, int i) {
        if (v < val) {
            val = v;
            index = i;
        }
    }

    @Override
    public String toString() {
        return String.format("%d=%d", index, val);
    }
}

