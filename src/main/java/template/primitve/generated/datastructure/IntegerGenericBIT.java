package template.primitve.generated.datastructure;

import java.util.Arrays;

public class IntegerGenericBIT {
    private int[] data;
    private int n;
    private IntegerBinaryFunction merger;
    private int unit;

    /**
     * 创建大小A[1...n]
     */
    public IntegerGenericBIT(int n, IntegerBinaryFunction merger, int unit) {
        this.n = n;
        data = new int[n + 1];
        this.merger = merger;
        this.unit = unit;
        clear();
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public int query(int i) {
        int sum = unit;
        for (; i > 0; i -= i & -i) {
            sum = merger.apply(sum, data[i]);
        }
        return sum;
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, int mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] = merger.apply(data[i], mod);
        }
    }

    public void undo(int i) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] = unit;
        }
    }

    /**
     * 将A全部清0
     */
    public void clear() {
        Arrays.fill(data, unit);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
