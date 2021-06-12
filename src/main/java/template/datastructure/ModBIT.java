package template.datastructure;

import template.math.Modular;
import template.math.DigitUtils;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class ModBIT {
    private long[] data;
    private int n;
    private int mod;
    Modular barrett;

    /**
     * 创建大小A[1...n]
     */
    public ModBIT(int n, int mod) {
        this.n = n;
        data = new long[n + 1];
        this.mod = mod;
        barrett = new Modular(mod);
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public int query(int i) {
        long sum = 0;
        for (; i > 0; i -= i & -i) {
            sum += data[i];
        }
        return barrett.valueOf(sum);
    }

    /**
     * 将A[i]更新为A[i]+modular
     */
    public void update(int i, int x) {
        if (i <= 0) {
            return;
        }
        x = DigitUtils.mod(x, mod);
        for (; i <= n; i += i & -i) {
            data[i] += x;
            if(data[i] >= mod){
                data[i] -= mod;
            }
        }
    }

    public int query(int l, int r) {
        int sum = query(r);
        if (l > 1) {
            sum -= query(l - 1);
            if(sum < 0){
                sum += mod;
            }
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
            builder.append(DigitUtils.mod(query(i) - query(i - 1), mod)).append(' ');
        }
        return builder.toString();
    }
}
