package template.datastructure;

import java.util.Arrays;

public class XorBIT {
    private long[] data;
    private int n;

    /**
     * 创建大小A[1...n]
     */
    public XorBIT(int n) {
        this.n = n;
        data = new long[n + 1];
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public long query(int i) {
        long sum = 0;
        for (; i > 0; i -= i & -i) {
            sum ^= data[i];
        }
        return sum;
    }

    public long query(int l, int r) {
        if (l > r) {
            return 0;
        }
        long ans = query(r);
        if (l > 1) {
            ans ^= query(l - 1);
        }
        return ans;
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, long mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] ^= mod;
        }
    }

    public void update(int l, int r, long mod) {
        update(l, mod);
        update(r + 1, mod);
    }

    /**
     * 将A全部清0
     */
    public void clear() {
        Arrays.fill(data, 0);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i) ^ query(i - 1)).append(' ');
        }
        return builder.toString();
    }
}
