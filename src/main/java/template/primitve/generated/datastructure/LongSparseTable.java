package template.primitve.generated.datastructure;

import template.math.CachedLog2;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class LongSparseTable {
    // st[i][j] means the merge value between [i, i + 2^j),
    // so st[i][j] equals to merge(st[i][j - 1], st[i + 2^(j - 1)][j - 1])
    private long[][] st;
    private LongBinaryFunction merger;

    public LongSparseTable(long[] data, int length, LongBinaryFunction merger) {
        int m = CachedLog2.floorLog(length);
        st = new long[m + 1][length];
        this.merger = merger;
        for (int i = 0; i < length; i++) {
            st[0][i] = data[i];
        }
        for (int i = 0; i < m; i++) {
            int interval = 1 << i;
            for (int j = 0; j < length; j++) {
                if (j + interval < length) {
                    st[i + 1][j] = merge(st[i][j], st[i][j + interval]);
                } else {
                    st[i + 1][j] = st[i][j];
                }
            }
        }
    }

    private long merge(long a, long b) {
        return merger.apply(a, b);
    }

    /**
     * query the merge value in [left,right]
     */
    public long query(int left, int right) {
        int queryLen = right - left + 1;
        int bit = CachedLog2.floorLog(queryLen);
        // x + 2^bit == right + 1
        // So x should be right + 1 - 2^bit - left=queryLen - 2^bit
        return merge(st[bit][left], st[bit][right + 1 - (1 << bit)]);
    }

    @Override
    public String toString() {
        return Arrays.toString(st[0]);
    }
}
