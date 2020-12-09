package on2020_12.on2020_12_08_AtCoder___AtCoder_Regular_Contest_110_Sponsored_by_KAJIMA_CORPORATION_.C___Exoswap;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CExoswap {
    int[] p;
    int[] inv;

    List<int[]> ops = new ArrayList<>();

    public void swap(int i, int j) {
        ops.add(new int[]{i, j});
        SequenceUtils.swap(p, i, j);
        SequenceUtils.swap(inv, p[i], p[j]);
        assert inv[p[i]] == i;
        assert inv[p[j]] == j;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        boolean[] used = new boolean[n];
        p = new int[n];
        inv = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
            inv[p[i]] = i;
        }
        for (int i = 0; i < n; i++) {
            assert inv[i] >= i;
            while (inv[i] != i) {
                if (used[inv[i]]) {
                    out.println(-1);
                    return;
                }
                used[inv[i]] = true;
                swap(inv[i], inv[i] - 1);
            }
        }
        if(ops.size() != n - 1){
            out.println(-1);
            return;
        }
        for (int[] op : ops) {
            out.println(Math.max(op[0], op[1]));
        }
    }
}
