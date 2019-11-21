package template.datastructure;

import template.math.CachedLog2;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * Created by dalt on 2018/5/20.
 */
public class SparseTable<T> {
    // st[i][j] means the merge value between [i, i + 2^j),
    // so st[i][j] equals to merge(st[i][j - 1], st[i + 2^(j - 1)][j - 1])
    private Object[][] st;
    private BiFunction<T, T, T> merger;
    private CachedLog2 log2;

    public SparseTable(Object[] data, int length, BiFunction<T, T, T> merger) {
        log2 = new CachedLog2(length);
        int m = log2.floorLog(length);

        st = new Object[m + 1][length];
        this.merger = merger;
        for (int i = 0; i < length; i++) {
            st[0][i] = data[i];
        }
        for (int i = 0; i < m; i++) {
            int interval = 1 << i;
            for (int j = 0; j < length; j++) {
                if (j + interval < length) {
                    st[i + 1][j] = merge((T) st[i][j], (T) st[i][j + interval]);
                } else {
                    st[i + 1][j] = st[i][j];
                }
            }
        }
    }

    private T merge(T a, T b) {
        return merger.apply(a, b);
    }

    /**
     * query the merge value in [left,right]
     */
    public T query(int left, int right) {
        int queryLen = right - left + 1;
        int bit = log2.floorLog(queryLen);
        // x + 2^bit == right + 1
        // So x should be right + 1 - 2^bit - left=queryLen - 2^bit
        return merge((T) st[bit][left], (T) st[bit][right + 1 - (1 << bit)]);
    }

    @Override
    public String toString() {
        return Arrays.toString(st[0]);
    }
}
