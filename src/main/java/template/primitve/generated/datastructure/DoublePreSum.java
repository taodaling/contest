package template.primitve.generated.datastructure;

public class DoublePreSum {
    private double[] pre;

    public DoublePreSum(int n) {
        pre = new double[n];
    }

    public void populate(double[] a){
        int n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a[i];
        }
    }

    public void populate(int[] a){
        int n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a[i];
        }
    }

    public DoublePreSum(double[] a) {
        this(a.length);
        populate(a);
    }

    public DoublePreSum(int[] a) {
        this(a.length);
        populate(a);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public double intervalSum(int l, int r) {
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
    public double prefix(int i) {
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public double post(int i) {
        if (i == 0) {
            return pre[pre.length - 1];
        }
        return pre[pre.length - 1] - pre[i - 1];
    }
}
