package template.datastructure;

import template.math.DigitUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class MergeAbleSegment implements Cloneable {
    private static final MergeAbleSegment NIL = new MergeAbleSegment();
    private static Deque<MergeAbleSegment> allocator = new ArrayDeque<>();

    public static MergeAbleSegment alloc() {
        return new MergeAbleSegment();
    }

    public static void destroy(MergeAbleSegment segment) {
        //allocator.addLast(segment);
    }

    static {
        NIL.left = NIL;
        NIL.right = NIL;
    }

    private MergeAbleSegment left;
    private MergeAbleSegment right;
    private int cnt;

    public void pushUp() {
        cnt = left.cnt + right.cnt;
    }

    public void pushDown() {
    }

    public MergeAbleSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int x, int l, int r, int mod) {
        if (l == r) {
            cnt += mod;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (x <= m) {
            if (left == NIL) {
                left = alloc();
            }
            left.update(x, l, m, mod);
        } else {
            if (right == NIL) {
                right = alloc();
            }
            right.update(x, m + 1, r, mod);
        }
        pushUp();
    }

    public int kth(int l, int r, int k) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (left.cnt >= k) {
            return left.kth(l, m, k);
        } else {
            return right.kth(m + 1, r, k - left.cnt);
        }
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return cnt;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    /**
     * split this by kth element, and kth element belong to the left part.
     * Return the k-th element as result
     */
    public MergeAbleSegment splitByKth(int k, int l, int r) {
        MergeAbleSegment ret = alloc();
        if (l == r) {
            ret.cnt = k;
            cnt -= k;
            return ret;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (k >= left.cnt) {
            k -= left.cnt;
            ret.left = left;
            left = NIL;
        } else {
            ret.left = left.splitByKth(k, l, m);
            k = 0;
        }
        if (k > 0) {
            if (k >= right.cnt) {
                ret.right = right;
                right = NIL;
            } else {
                ret.right = right.splitByKth(k, l, m);
            }
        }

        ret.pushUp();
        this.pushUp();
        return ret;
    }

    public MergeAbleSegment merge(int l, int r, MergeAbleSegment segment) {
        if (this == NIL) {
            return segment;
        } else if (segment == NIL) {
            return this;
        }
        if (l == r) {
            cnt += segment.cnt;
            destroy(segment);
            return this;
        }
        int m = DigitUtils.floorAverage(l, r);
        left = left.merge(l, m, segment.left);
        right = right.merge(m + 1, r, segment.right);
        destroy(segment);
        pushUp();
        return this;
    }
}