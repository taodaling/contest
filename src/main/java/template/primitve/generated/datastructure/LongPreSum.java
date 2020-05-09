package template.primitve.generated.datastructure;

public class LongPreSum {
    private long[] pre;
    private int n;

    public LongPreSum(int n) {
        pre = new long[n];
    }

    public void populate(IntToLongFunction a, int n) {
        this.n = n;
        pre[0] = a.apply(0);
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a.apply(i);
        }
    }

    public LongPreSum(IntToLongFunction a, int n) {
        this(n);
        populate(a, n);
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(intervalSum(i, i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
