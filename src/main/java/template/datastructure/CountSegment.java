package template.datastructure;

public class CountSegment implements Cloneable {
    private static final CountSegment NIL = new CountSegment();
    private CountSegment left;
    private CountSegment right;
    private int cnt;

    public void pushUp() {
        cnt = left.cnt + right.cnt;
    }

    public void pushDown() {
        left = left.clone();
        right = right.clone();
    }

    public CountSegment() {
        left = right = this;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int x, int l, int r, int mod) {
        if (noIntersection(x, x, l, r)) {
            return;
        }
        if (covered(x, x, l, r)) {
            cnt++;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(x, l, m, mod);
        right.update(x, m + 1, r, mod);
        pushUp();
    }

    public int kth(int l, int r, int k) {
        if (l == r) {
            return l;
        }
        int m = (l + r) >> 1;
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
        int m = (l + r) >> 1;
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    public CountSegment merge(int l, int r, CountSegment segment) {
        if (this == NIL) {
            return segment;
        } else if (segment == NIL) {
            return this;
        }
        if (l == r) {
            cnt += segment.cnt;
        }
        int m = (l + r) >> 1;
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
