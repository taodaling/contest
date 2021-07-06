package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class RectSum {
    LongArrayList xall;
    LongArrayList yall;
    int numY;
    int numX;
    NoTagPersistentSegment[] sts;
    static long inf = (long) 4e18;

    /**
     * O(n\log_2n) time and memory
     *
     * @param xs
     * @param ys
     * @param vals
     */
    public RectSum(long[] xs, long[] ys, long[] vals) {
        xall = new LongArrayList(xs.length + 1);
        xall.add(-inf);
        xall.addAll(xs);
        yall = new LongArrayList(ys);
        xall.unique();
        yall.unique();
        numX = xall.size();
        numY = yall.size();
        int n = xs.length;
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Long.compare(xs[i], xs[j]), 0, n);
        sts = new NoTagPersistentSegment[xall.size()];
        sts[0] = new NoTagPersistentSegment();
        for (int i = 1, j = 0; i < sts.length; i++) {
            int l = j;
            while (j < n && xs[indices[j]] == xs[indices[l]]) {
                j++;
            }
            sts[i] = sts[i - 1].clone();
            int r = j - 1;
            for (int k = l; k <= r; k++) {
                int index = indices[k];
                int y = yall.binarySearch(ys[index]);
                sts[i].update(y, y, 0, numY - 1, vals[index]);
            }
        }
    }

    /**
     * Count the sum of all points located in rect
     * <p>
     * O(\log_2n)
     *
     * @param l
     * @param r
     * @param b
     * @param t
     * @return
     */
    public long query(long l, long r, long b, long t) {
        int lIndex = xall.lowerBound(l) - 1;
        int rIndex = xall.upperBound(r) - 1;
        int bIndex = yall.lowerBound(b);
        int tIndex = yall.upperBound(t) - 1;
        if (lIndex == rIndex || bIndex > tIndex) {
            return 0;
        }
        return sts[rIndex].query(bIndex, tIndex, 0, numY - 1) -
                sts[lIndex].query(bIndex, tIndex, 0, numY - 1);
    }

    static class NoTagPersistentSegment implements Cloneable {
        public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

        static {
            NIL.left = NIL.right = NIL;
        }

        private NoTagPersistentSegment left;
        private NoTagPersistentSegment right;
        long sum;

        public void pushUp() {
            if (this == NIL) {
                return;
            }
            sum = left.sum + right.sum;
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

        public void update(int ll, int rr, int l, int r, long x) {
            if (covered(ll, rr, l, r)) {
                sum += x;
                return;
            }
            int m = DigitUtils.floorAverage(l, r);
            if (!noIntersection(ll, rr, l, m)) {
                left = left.clone();
                left.update(ll, rr, l, m, x);
            }
            if (!noIntersection(ll, rr, m + 1, r)) {
                right = right.clone();
                right.update(ll, rr, m + 1, r, x);
            }
            pushUp();
        }

        public long query(int ll, int rr, int l, int r) {
            if (this == NIL || noIntersection(ll, rr, l, r)) {
                return 0;
            }
            if (covered(ll, rr, l, r)) {
                return sum;
            }
            int m = DigitUtils.floorAverage(l, r);
            return left.query(ll, rr, l, m) +
                    right.query(ll, rr, m + 1, r);
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
