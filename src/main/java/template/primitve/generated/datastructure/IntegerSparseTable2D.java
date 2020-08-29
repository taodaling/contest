package template.primitve.generated.datastructure;

import template.binary.Log2;
import template.utils.ArrayIndex;

public class IntegerSparseTable2D {
    private ArrayIndex ai;
    private int[] data;
    private IntegerBinaryFunction merger;

    public IntegerSparseTable2D(Int2ToIntegerFunction mat, int n, int m, IntegerBinaryFunction merger) {
        this.merger = merger;
        int logn = Log2.floorLog(n);
        int logm = Log2.floorLog(m);
        ai = new ArrayIndex(logn + 1, n, logm + 1, m);
        data = new int[ai.totalSize()];


        for (int i = 0; i <= logn; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= logm; k++) {
                    for (int t = 0; t < m; t++) {
                        if (i == 0 && k == 0) {
                            data[ai.indexOf(i, j, k, t)] = mat.apply(j, t);
                        } else if (i == 0) {
                            data[ai.indexOf(i, j, k, t)] = merger.apply(
                                    data[ai.indexOf(i, j, k - 1, t)],
                                    data[ai.indexOf(i, j, k - 1, Math.min(t + (1 << k - 1), m - 1))]
                            );
                        } else if (k == 0) {
                            data[ai.indexOf(i, j, k, t)] = merger.apply(
                                    data[ai.indexOf(i - 1, j, k, t)],
                                    data[ai.indexOf(i - 1, Math.min(j + (1 << i - 1), n - 1), k, t)]
                            );
                        } else {
                            int ans = merger.apply(data[ai.indexOf(i - 1, j, k, t)],
                                    data[ai.indexOf(i - 1, Math.min(j + (1 << i - 1), n - 1), k, t)]);
                            ans = merger.apply(data[ai.indexOf(i - 1, j, k - 1, Math.min(t + (1 << k - 1), m - 1))], ans);
                            ans = merger.apply(data[ai.indexOf(i - 1, Math.min(j + (1 << i - 1), n - 1), k - 1, Math.min(t + (1 << k - 1), m - 1))], ans);
                            data[ai.indexOf(i, j, k, t)] = ans;
                        }
                    }
                }
            }
        }
    }

    public int query(int b, int t, int l, int r) {
        int h = t - b + 1;
        int w = r - l + 1;
        int logh = Log2.floorLog(h);
        int logw = Log2.floorLog(w);

        int ans = merger.apply(data[ai.indexOf(logh, b, logw, l)],
                data[ai.indexOf(logh, t + 1 - (1 << logh), logw, l)]);
        ans = merger.apply(data[ai.indexOf(logh, b, logw, r + 1 - (1 << logw))], ans);
        ans = merger.apply(data[ai.indexOf(logh, t + 1 - (1 << logh), logw, r + 1 - (1 << logw))], ans);
        return ans;
    }
}
