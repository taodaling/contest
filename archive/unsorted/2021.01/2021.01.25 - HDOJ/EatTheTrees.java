package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.GridUtils;

import java.util.Arrays;

public class EatTheTrees {
    int mask;

    public int get(int s, int a, int b) {
        return (((s & ~1) << 1) | (a << 1) | b) & mask;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }
        long[] prev = new long[1 << m + 1];
        long[] next = new long[1 << m + 1];

        mask = prev.length - 1;
        prev[0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(next, 0);
                for (int k = 0; k < 1 << m + 1; k++) {
                    if(prev[k] == 0){
                        continue;
                    }
                    int last = Bits.get(k, 0);
                    int first = Bits.get(k, m);
                    if (j == 0 && last != 0) {
                        continue;
                    }
                    if (mat[i][j] == 0) {
                        if (first == 0 && last == 0) {
                            next[get(k, 0, 0)] += prev[k];
                        }
                        continue;
                    }
                    for (int[] trans : GridUtils.OUTLINE_TRANSFORM) {
                        if (first == trans[0] && last == trans[1]) {
                            next[get(k, trans[2], trans[3])] += prev[k];
                        }
                    }

                }
//                debug.debug("i", i);
//                debug.debug("j", j);
//                for (int k = 0; k < 1 << m + 1; k++) {
//                    debug.debug(Integer.toBinaryString(k), next[k]);
//                }
                long[] tmp = prev;
                prev = next;
                next = tmp;
            }
        }

        long ans = prev[0];
        out.printf("Case %d: There are %d ways to eat the trees.", testNumber, ans).println();
    }
}
