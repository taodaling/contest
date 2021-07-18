package template.datastructure;

import template.utils.Sum;

import java.util.function.Supplier;

public class GenericBIT<S extends Sum<S>> {
    private Object[] data;
    private int n;

    /**
     * 创建大小A[1...n]
     */
    public GenericBIT(int n, Supplier<S> supplier) {
        this.n = n;
        data = new Object[n + 1];
        for (int i = 1; i <= n; i++) {
            data[i] = supplier.get();
        }
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public void query(int i, S sum) {
        for (; i > 0; i -= i & -i) {
            sum.add((S) data[i]);
        }
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, S mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            ((S) data[i]).add(mod);
        }
    }

}
