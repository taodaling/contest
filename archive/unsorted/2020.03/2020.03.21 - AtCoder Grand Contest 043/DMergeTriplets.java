package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Factorial;
import template.math.Modular;
import template.utils.Debug;

public class DMergeTriplets {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(m);

        int total = 3 * n;
        int[][] last = new int[total][3];
        int[][] cur = new int[total][3];
        for (int i = 0; i < total; i++) {
            last[i][0] = 1;
        }

        debug.debug("last", last);
        int[] sum = new int[total];
        for (int i = 1; i < total; i++) {
            for (int j = 0; j < total; j++) {
                cur[j][1] = mod.mul(last[j][0], Math.max(0, j - i + 1));
                cur[j][2] = mod.mul(last[j][1], Math.max(0, j - i + 1));
            }
            for (int j = 1; j < total; j++) {
                sum[j] = mod.plus(last[j - 1][1], last[j - 1][0]);
                sum[j] = mod.plus(sum[j], last[j - 1][2]);
                sum[j] = mod.plus(sum[j], sum[j - 1]);
            }

            for (int j = 0; j < total; j++) {
                cur[j][0] = sum[j];
            }

            int[][] tmp = last;
            last = cur;
            cur = tmp;

            debug.debug("last", last);
        }

        int ans = 0;
        for (int i = 0; i < 3; i++) {
            ans = mod.plus(last[total - 1][i], ans);
        }

        out.println(ans);
    }
}
