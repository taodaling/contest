package contest;

import template.FastInput;
import template.FastOutput;
import template.IntDeque;

import java.util.Arrays;

public class TaskF {
    Segment seg;
    MinMaxSegment valSeg;
    int n;
    int[] valToIndex;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();

        if (n == 1) {
            out.append("1 0");
            return;
        }
        int[] perm = new int[1 + n * 2];
        valToIndex = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            perm[i + n] = perm[i] = in.readInt();
            valToIndex[perm[i]] = i;
        }

        seg = new Segment(1, 2 * n);
        valSeg = new MinMaxSegment(1, n, perm);
        buildTree(1, n);
        Recoder recoder = new Recoder();
        recoder.record(seg.queryMax(1, n, 1, 2 * n), 0);

        int[] rightIndex = new int[2 * n + 1];
        IntDeque deque = new IntDeque(2 * n);
        for (int i = 2 * n; i >= 1; i--) {
            while (!deque.isEmpty() && perm[deque.peekFirst()] >= perm[i]) {
                deque.removeFirst();
            }
            if (deque.isEmpty()) {
                rightIndex[i] = 2 * n + 1;
            } else {
                rightIndex[i] = deque.peekFirst();
            }
            deque.addFirst(i);
        }

        IntDeque rightSide = new IntDeque(2 * n);
        for (int i = 1; i <= n; i++) {
            while (!rightSide.isEmpty() && perm[rightSide.peekLast()] >= perm[i]) {
                rightSide.removeLast();
            }
            rightSide.addLast(i);
        }

        for (int i = 1; i <= n; i++) {
            int r = rightIndex[i];
            if (rightSide.peekFirst() == i) {
                rightSide.removeFirst();
                seg.update(i + 1, n - 1 + i, 1, 2 * n, -1);
            } else {
                seg.update(i + 1, r - 1, 1, 2 * n, -1);
            }

            while (!rightSide.isEmpty() && perm[rightSide.peekLast()] >= perm[i]) {
                rightSide.removeLast();
            }
            if (rightSide.isEmpty()) {
                seg.update(i + 1, n - 1 + i, 1, 2 * n, 1);
                //seg.update(i + n, i + n, 1 , 2 * n, 0);
            } else {
                seg.update(rightSide.peekLast() + 1, n - 1 + i, 1, 2 * n, 1);
                int fatherDepth = seg.queryMax(rightSide.peekLast(), rightSide.peekLast(), 1, 2 * n);
                seg.update(i + n, i + n, 1, 2 * n, fatherDepth + 1);
            }
            rightSide.addLast(i + n);
            recoder.record(seg.queryMax(i + 1, i + n, 1, 2 * n), i);
        }

        out.append(recoder.height + 1).append(' ').append(recoder.offset);
    }

    public void buildTree(int l, int r) {
        int minVal = valSeg.query(l, r, 1, n);
        int m = valToIndex[minVal];
        if (l < m) {
            buildTree(l, m - 1);
            seg.update(l, m - 1, 1, 2 * n, 1);
        }
        if (r > m) {
            buildTree(m + 1, r);
            seg.update(m + 1, r, 1, 2 * n, 1);
        }
    }

}

class MinMaxSegment implements Cloneable {
    private MinMaxSegment left;
    private MinMaxSegment right;
    private int val;

    public void pushUp() {
        val = Math.min(left.val, right.val);
    }

    public void pushDown() {
    }

    public MinMaxSegment(int l, int r, int[] p) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new MinMaxSegment(l, m, p);
            right = new MinMaxSegment(m + 1, r, p);
            pushUp();
        } else {
            val = p[l];
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return Integer.MAX_VALUE;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int height;
    private int mod;

    public void mod(int x) {
        mod += x;
        height += x;
    }

    public void pushUp() {
        height = Math.max(left.height, right.height);
    }

    public void pushDown() {
        if (mod != 0) {
            left.mod(mod);
            right.mod(mod);
            mod = 0;
        }
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

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            mod(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return height;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }
}

class Recoder {
    int height = Integer.MAX_VALUE;
    int offset = -1;

    public void record(int h, int o) {
        if (h < height) {
            height = h;
            offset = o;
        }
    }

    @Override
    public String toString() {
        return offset + "=" + height;
    }
}
