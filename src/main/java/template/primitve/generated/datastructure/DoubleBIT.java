package template.primitve.generated.datastructure;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class DoubleBIT {
    private double[] data;
    private int n;

    /**
     * 创建大小A[1...n]
     */
    public DoubleBIT(int n) {
        this.n = n;
        data = new double[n + 1];
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public double query(int i) {
        double sum = 0;
        for (; i > 0; i -= i & -i) {
            sum += data[i];
        }
        return sum;
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, double mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data[i] += mod;
        }
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
