package template.primitve.generated.datastructure;

public class IntegerPreSum {
    private int[] pre;
    private int n;

    public IntegerPreSum(int n) {
        pre = new int[n];
    }

    public void populate(IntToIntegerFunction a, int n) {
        this.n = n;
        if (n == 0) {
            return;
        }
        pre[0] = a.apply(0);
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a.apply(i);
        }
    }

    public IntegerPreSum(IntToIntegerFunction a, int n) {
        this(n);
        populate(a, n);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public int intervalSum(int l, int r) {
        if (r < l) {
            return 0;
        }
        return prefix(r) - prefix(l - 1);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        i = Math.min(i, n - 1);
        if (i < 0) {
            return 0;
        }
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int post(int i) {
        return prefix(n) - prefix(i - 1);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            ans.append(intervalSum(i, i)).append(',');
        }
        if (ans.length() > 0) {
            ans.setLength(ans.length() - 1);
        }
        return ans.toString();
    }
}
