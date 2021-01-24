package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class P2744USACO53MilkMeasuring {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        n = in.ri();
        size = in.ri(n);
        Randomized.shuffle(size);
        Arrays.sort(size);
        SequenceUtils.reverse(size);
        debug.debug("size", size);
        dp = new int[n + 1][q + 1];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int s = size[i];
            for (int j = 0; j < s; j++) {
                int min = j;
                for (int k = j; k <= q; k += s) {
                    if (dp[i][min] > dp[i][k]) {
                        min = k;
                    }
                    dp[i + 1][k] = Math.min(dp[i][k], dp[i][min] + 1);
                }
            }
        }
        debug.debug("dp", dp);
        out.append(dp[n][q]).append(' ');
        sets = new BitSet[n + 1][q + 1];
        tmp = new BitSet(n);
        BitSet ans = trace(n, q);
        for (int i = n - 1; i >= 0; i--) {
            if (ans.get(i)) {
                out.append(size[i]).append(' ');
            }
        }
    }

    int n;
    BitSet[][] sets;
    int[][] dp;
    int[] size;

    public BitSet trace(int i, int j) {
        if (sets[i][j] == null) {
            if (i == 0) {
                return sets[i][j] = new BitSet(n);
            }
            if (dp[i - 1][j] == dp[i][j]) {
                sets[i][j] = trace(i - 1, j).clone();
            }
            for (int k = j; k >= 0; k -= size[i - 1]) {
                if (dp[i - 1][k] + 1 == dp[i][j]) {
                    BitSet cand = trace(i - 1, k).clone();
                    cand.set(i - 1);
                    sets[i][j] = max(sets[i][j], cand);
                }
            }
        }
        return sets[i][j];
    }

    BitSet tmp;

    public BitSet max(BitSet a, BitSet b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        tmp.copy(a);
        tmp.xor(b);
        int first = tmp.prevSetBit(tmp.capacity() - 1);
        if (first < 0) {
            return a;
        }
        return a.get(first) ? a : b;
    }
}
