package template.primitve.generated.datastructure;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class LongBIT {
    /**
     * data[i] = \sum_{j \in (i-lowbit(i), i]} A[i]
     */
    private long[] data;
    private int n;

    /**
     * 创建大小A[1...n]
     */
    public LongBIT(int n) {
        this.n = n;
        data = new long[n + 1];
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public long query(int i) {
        i = Math.min(i, data.length - 1);
        long sum = 0;
        for (; i > 0; i -= i & -i) {
            sum += data[i];
        }
        return sum;
    }

    public long query(int l, int r) {
        return query(r) - query(l - 1);
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, long mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] += mod;
        }
    }

    public int size() {
        return n;
    }

    /**
     * 将A[i]更新为x
     */
    public void set(int i, long x) {
        update(i, x - query(i, i));
    }

    /**
     * 将A全部清0
     */
    public void clear(int n) {
        this.n = n;
        Arrays.fill(data, 1, n + 1, 0);
    }

    public void clear() {
        clear(n);
    }

    public void clear(IntToLongFunction function, int n) {
        this.n = n;
        for (int i = 1; i <= n; i++) {
            data[i] = function.apply(i);
        }
        for (int i = 1; i <= n; i++) {
            int to = i + (i & -i);
            if (to <= n) {
                data[to] += data[i];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i) - query(i - 1)).append(' ');
        }
        return builder.toString();
    }
}
