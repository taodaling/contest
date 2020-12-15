package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class AAlternativeThinking {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        int[][] prev = new int[3][2];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(prev, -inf);
        int[][] next = new int[3][2];
        prev[0][0] = 0;
        prev[0][1] = 0;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 2; k++) {
                int max = 0;
                for (int j = 0; j < 3; j++) {
                    max = Math.max(prev[j][k], max);
                    prev[j][k] = max;
                }
            }
            debug.debug("i", i);
            debug.debug("prev", prev);
            SequenceUtils.deepFill(next, -inf);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 2; k++) {
                    int x = s[i] - '0';
                    x ^= j & 1;
                    //do nothing
                    next[j][k] = Math.max(next[j][k], prev[j][k]);
                    //added
                    if (k != x) {
                        next[j][x] = Math.max(next[j][x], prev[j][k] + 1);
                    }
                }

            }
            int[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.debug("i", n);
        debug.debug("prev", prev);
        int max = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 2; k++) {
                max = Math.max(max, prev[j][k]);
            }
        }
        out.println(max);
    }
}
