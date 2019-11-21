package template.datastructure;

/**
 * This class represent an array : a[0], a[1], ... , a[n - 1].
 * <br>
 * You can add a value to all the element in the array, or
 * truncate the low bound or the high bound
 */
public class BulkOperationArray implements Cloneable {
    private long l;
    private long r;
    private long lCnt;

    public BulkOperationArray(long l, long r) {
        this.l = l;
        this.r = r;
        this.lCnt = 1;
    }

    public void truncateMin(long min) {
        if (l >= min) {
            return;
        }
        if (r <= min) {
            l = r = min;
            return;
        }
        lCnt += min - l;
        l = min;
    }

    public void truncateMax(long max) {
        if (r <= max) {
            return;
        }
        if (l >= max) {
            l = r = max;
            return;
        }
        r = max;
    }

    public void bulkAdd(long x) {
        l += x;
        r += x;
    }

    public void bulkSubtract(long x) {
        bulkAdd(-x);
    }

    public long get(long i) {
        if (l == r) {
            return l;
        }
        if (i < lCnt) {
            return l;
        }
        i -= lCnt;
        if (i < r - l - 1) {
            return i + l + 1;
        }
        return r;
    }

    @Override
    public BulkOperationArray clone() {
        try {
            return (BulkOperationArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
