package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.utils.ArrayIndex;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CRangeSet {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        a = in.readInt();
        b = in.readInt();
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        f = new int[a];
        SequenceUtils.deepFill(f, -1);

//        for (int i = 0; i < a; i++) {
//            debug.debug("f(" + i + ")", f(i));
//        }

        ArrayIndex ai = new ArrayIndex(n + 1, a, 2);
        int[] dp = new int[ai.totalSize()];
        dp[ai.indexOf(0, 0, 0)] = 1;
        dp[ai.indexOf(0, 0, 1)] = 1;
        int[] sum = new int[2];
        for (int i = 1; i <= n; i++) {
            Arrays.fill(sum, 0);
            for (int j = 0; j < a; j++) {
                int lend = j == i - 1 ? 1 : 0;
                sum[0] = mod.plus(sum[0], mod.mul(dp[ai.indexOf(i - 1, j, 0)], f(j - 2 + lend)));
            }
            for (int j = 0; j < b; j++) {
                sum[1] = mod.plus(sum[1], dp[ai.indexOf(i - 1, j, 1)]);
            }
            debug.debug("i", i - 1);
            debug.debug("sum", sum);
            for (int k = 1; k < a; k++) {
                if (k == 1) {
                    dp[ai.indexOf(i, k, 0)] = sum[1];
                } else {
                    dp[ai.indexOf(i, k, 0)] = dp[ai.indexOf(i - 1, k - 1, 0)];
                }
            }
            for (int k = 1; k < b; k++) {
                if (k == 1) {
                    dp[ai.indexOf(i, k, 1)] = sum[0];
                } else {
                    dp[ai.indexOf(i, k, 1)] = dp[ai.indexOf(i - 1, k - 1, 1)];
                }
            }
        }

        Arrays.fill(sum, 0);
        for (int j = 0; j < a; j++) {
            sum[0] = mod.plus(sum[0], mod.mul(dp[ai.indexOf(n, j, 0)], f(j - 1)));
        }
        for (int j = 0; j < b; j++) {
            sum[1] = mod.plus(sum[1], dp[ai.indexOf(n, j, 1)]);
        }
        debug.debug("sum", sum);

        int invalid = mod.plus(sum[0], sum[1]);
        int total = pow.pow(2, n);
        int valid = mod.subtract(total, invalid);
        out.println(valid);
    }


    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    int a;
    int b;

    int[] f;

    public int f(int i) {
        if (i <= 0) {
            return 1;
        }
        if (f[i] == -1) {
            f[i] = f(i - 1);
            for (int t = b; i - t >= 0; t++) {
                f[i] = mod.plus(f[i], f(i - t - 1));
            }
        }
        return f[i];
    }
}
