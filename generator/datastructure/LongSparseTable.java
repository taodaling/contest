package template.primitve.generated.datastructure;

import template.binary.Log2;

import java.util.Arrays;

/**
 * Created by dalt on 2018/5/20.
 */
public class LongSparseTable {
    // st[i][j] means the merge value between [i, i + 2^j),
    // so st[i][j] equals to merge(st[i][j - 1], st[i + 2^(j - 1)][j - 1])
    private long[][] st;
    private LongBinaryFunction merger;
    int m;
    public void init(int length, IntToLongFunction function, LongBinaryFunction merger){
        this.merger = merger;
        for (int i = 0; i < length; i++) {
            st[0][i] = function.apply(i);
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

    public LongSparseTable(int length) {
        m = Log2.floorLog(length);
        st = new long[m + 1][length];
    }

    public LongSparseTable(int length,  IntToLongFunction function, LongBinaryFunction merger) {
        this(length);
        init(length, function, merger);
    }

    private long merge(long a, long b) {
        return merger.apply(a, b);
    }

    /**
     * query the merge value in [left,right]
     */
    public long query(int left, int right) {
        int queryLen = right - left + 1;
        int bit = Log2.floorLog(queryLen);
        // x + 2^bit == right + 1
        // So x should be right + 1 - 2^bit - left=queryLen - 2^bit
        return merge(st[bit][left], st[bit][right + 1 - (1 << bit)]);
    }

    @Override
    public String toString() {
        return Arrays.toString(st[0]);
    }
}
