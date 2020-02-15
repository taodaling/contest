package template.datastructure;

import template.math.Modular;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class ModBIT {
    private int[] data;
    private int n;
    private Modular modular;

    /**
     * 创建大小A[1...n]
     */
    public ModBIT(int n, Modular mod) {
        this.n = n;
        data = new int[n + 1];
        this.modular = mod;
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public int query(int i) {
        long sum = 0;
        for (; i > 0; i -= i & -i) {
            sum += data[i];
        }
        return modular.valueOf(sum);
    }

    /**
     * 将A[i]更新为A[i]+modular
     */
    public void update(int i, int mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] = modular.plus(data[i], mod);
        }
    }

    public int interval(int l, int r) {
        int sum = query(r);
        if (l > 1) {
            sum = modular.subtract(sum, query(l - 1));
        }
        return sum;
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
            builder.append(query(i) - query(i - 1)).append(' ');
        }
        return builder.toString();
    }
}
