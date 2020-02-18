package template.primitve.generated.datastructure;

public class IntegerPreSum {
    private int[] pre;

    public IntegerPreSum(int n) {
        pre = new int[n];
    }

    public void populate(int[] a){
        int n = a.length;
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
        if (l > r) {
            return 0;
        }
        if (l == 0) {
            return pre[r];
        }
        return pre[r] - pre[l - 1];
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int post(int i) {
        if (i == 0) {
            return pre[pre.length - 1];
        }
        return pre[pre.length - 1] - pre[i - 1];
    }
}
