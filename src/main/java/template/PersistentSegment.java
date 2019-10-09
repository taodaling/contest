package template;

public class PersistentSegment implements Cloneable {
    private static final PersistentSegment NIL = new PersistentSegment();
    private PersistentSegment left;
    private PersistentSegment right;

    public void pushUp() {
    }

    public void pushDown() {
        left = left.clone();
        right = right.clone();
    }

    public PersistentSegment() {
        left = right = this;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m);
        right.update(ll, rr, m + 1, r);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    @Override
    public PersistentSegment clone() {
        try {
            return (PersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
