package template.datastructure;

import template.math.DigitUtils;

public class PersistentArray<T> {
    private NoTagPersistentSegment<T> root;
    private int n;

    public PersistentArray(int n) {
        root = NoTagPersistentSegment.NIL;
        this.n = n;
    }

    public T get(int x) {
        return root.query(x, x, 0, n - 1);
    }

    public PersistentArray<T> set(int x, T val) {
        PersistentArray<T> ans = new PersistentArray<>(n);
        ans.root = root.clone();
        ans.root.update(x, x, 0, n - 1, val);
        return ans;
    }

    public int length() {
        return n;
    }

    public PersistentArray<T> fill(T val) {
        return fill(0, n - 1, val);
    }

    public PersistentArray<T> fill(int l, int r, T val) {
        PersistentArray<T> ans = new PersistentArray<>(n);
        ans.root = root.clone();
        ans.root.update(l, r, 0, n - 1, val);
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (int i = 0; i < n; i++) {
            ans.append(get(i)).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("]");
        return ans.toString();
    }

    private static class NoTagPersistentSegment<T> implements Cloneable {
        public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

        static {
            NIL.left = NIL.right = NIL;
        }

        private NoTagPersistentSegment<T> left;
        private NoTagPersistentSegment<T> right;
        private T val;

        public void pushUp() {
        }

        public NoTagPersistentSegment() {
            left = right = NIL;
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, T val) {
            if (covered(ll, rr, l, r)) {
                if (l == r) {
                    this.val = val;
                    return;
                }
            }
            int m = DigitUtils.floorAverage(l, r);
            if (!noIntersection(ll, rr, l, m)) {
                left = left.clone();
                left.update(ll, rr, l, m, val);
            }
            if (!noIntersection(ll, rr, m + 1, r)) {
                right = right.clone();
                right.update(ll, rr, m + 1, r, val);
            }
            pushUp();
        }

        public T query(int ll, int rr, int l, int r) {
            if (this == NIL || noIntersection(ll, rr, l, r)) {
                return null;
            }
            if (covered(ll, rr, l, r)) {
                return val;
            }
            int m = DigitUtils.floorAverage(l, r);
            T ans = left.query(ll, rr, l, m);
            if (ans == null) {
                ans = right.query(ll, rr, m + 1, r);
            }
            return ans;
        }

        @Override
        public NoTagPersistentSegment clone() {
            try {
                return (NoTagPersistentSegment) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
