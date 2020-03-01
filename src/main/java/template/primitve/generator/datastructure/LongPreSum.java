package template.primitve.generated.datastructure;

public class LongPreSum {
    private long[] pre;
    private int n;

    public LongPreSum(int n) {
        pre = new long[n];
    }

    public void populate(long[] a) {
        n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a[i];
        }
    }

    public LongPreSum(long[] a) {
        this(a.length);
        populate(a);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public long intervalSum(int l, int r) {
        return prefix(r) - prefix(l - 1);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public long prefix(int i) {
        if (i < 0) {
            return 0;
        }
        return pre[Math.min(i, n - 1)];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public long post(int i) {
        return pre[n - 1] - prefix(i - 1);
    }
}
