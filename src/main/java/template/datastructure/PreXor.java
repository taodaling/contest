package template.datastructure;

public class PreXor {
    private long[] pre;
    private int n;

    public PreXor(int n) {
        pre = new long[n];
    }

    public void populate(long[] a, int n) {
        this.n = n;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] ^ a[i];
        }
    }

    public void populate(int[] a, int n) {
        this.n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] ^ a[i];
        }
    }

    public PreXor(long[] a) {
        this(a.length);
        populate(a, a.length);
    }

    public PreXor(int[] a) {
        this(a.length);
        populate(a, a.length);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public long intervalSum(int l, int r) {
        return prefix(r) ^ prefix(l - 1);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public long prefix(int i) {
        i = Math.min(i, n - 1);
        if (i < 0) {
            return 0;
        }
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public long post(int i) {
        return prefix(n) ^ prefix(i - 1);
    }
}
