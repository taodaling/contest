package template.primitve.generated.datastructure;

public class IntegerPreSum {
    private int[] pre;
    private int n;

    public IntegerPreSum(int n) {
        pre = new int[n];
    }

    public void populate(int[] a) {
        n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a[i];
        }
    }

    public IntegerPreSum(int[] a) {
        this(a.length);
        populate(a);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public int intervalSum(int l, int r) {
        return prefix(r) - prefix(l - 1);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        if (i < 0) {
            return 0;
        }
        return pre[Math.min(i, n - 1)];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int post(int i) {
        return pre[n - 1] - prefix(i - 1);
    }
}
