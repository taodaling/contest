package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.HashMap;
import java.util.Map;

public class FDoubleKnapsack {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] A = in.rl(n);
        long[] B = in.rl(n);
        long[] prefA = new long[n + 1];
        long[] prefB = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            prefA[i] = prefA[i - 1] + A[i - 1];
            prefB[i] = prefB[i - 1] + B[i - 1];
        }
        int L = 0;
        boolean swap = false;
        if (prefB[n] < prefA[n]) {
            swap = true;
            long[] tmp = prefB;
            prefB = prefA;
            prefA = tmp;
        }
        Map<Long, int[]> map = new HashMap<>(n);
        map.put(0L, new int[2]);
        for (int i = 1; i <= n; i++) {
            while (L + 1 < n && prefB[L + 1] <= prefA[i]) {
                L++;
            }
            int[] cand = new int[]{i, L};
            int[] old = map.put(prefA[i] - prefB[L], cand);
            if (old != null) {
                if (swap) {
                    SequenceUtils.swap(cand, 0, 1);
                    SequenceUtils.swap(old, 0, 1);
                }
                for (int k = 0; k < 2; k++) {
                    out.println(cand[k] - old[k]);
                    for (int j = old[k] + 1; j <= cand[k]; j++) {
                        out.append(j).append(' ');
                    }
                    out.println();
                }
                return;
            }
        }
        out.println(-1);
    }
}
