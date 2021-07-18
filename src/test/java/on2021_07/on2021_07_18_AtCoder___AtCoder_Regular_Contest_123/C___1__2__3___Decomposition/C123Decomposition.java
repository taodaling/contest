package on2021_07.on2021_07_18_AtCoder___AtCoder_Regular_Contest_123.C___1__2__3___Decomposition;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class C123Decomposition {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.rs();
        char[] cs = s.toCharArray();
        int n = cs.length;
        boolean[][] prev = new boolean[6][2];
        boolean[][] next = new boolean[6][2];
        SequenceUtils.deepFill(prev, false);
        prev[0][0] = true;
        for (char c : cs) {
            debug.debug("c", c);
            debug.debug("prev", prev);
            c -= '0';
            SequenceUtils.deepFill(next, false);
            for (int i = 0; i <= 5; i++) {
                for (int j = 0; j < 2; j++) {
                    if (!prev[i][j]) {
                        continue;
                    }
                    for (int t = i; t <= 5; t++) {
                        for (int z = 0; z < 2; z++) {
                            int val = j * 10 + c - z;
                            if (t <= val && 3 * t >= val) {
                                next[t][z] = true;
                            }
                        }
                    }
                }
            }
            boolean[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.log("end");
        debug.debug("prev", prev);
        for (int i = 0; i <= 5; i++) {
            if (prev[i][0]) {
                out.println(i);
                return;
            }
        }
    }
}
