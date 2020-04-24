package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class TaskD {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        for (int i = 1; i <= 9 && k > 0; i++, k--) {
            out.println(i);
        }
        for (int i = 1; i <= 9 && k > 0; i++, k--) {
            out.println(i * 10 + 9);
        }
        for (int len = 3; k > 0; len++) {
            long base = 1;
            for (int i = 0; i < len - 3; i++) {
                base *= 10;
            }
            long[] val = new long[1000];
            double[] div = new double[1000];
            double[] min = new double[1000];
            for (int i = 100; i <= 999; i++) {
                val[i] = (i + 1) * base - 1;
                div[i] = val[i] / (double) sum(val[i]);
            }
            min[999] = div[999];
            for (int i = 998; i >= 100; i--) {
                min[i] = Math.min(min[i + 1], div[i]);
            }

            for (int i = 100; i <= 999 && k > 0; i++) {
                if (min[i] == div[i]) {
                    k--;
                    out.println(val[i]);
                }
            }
        }
    }

    public long sum(long x) {
        return x == 0 ? 0 : (x % 10 + sum(x / 10));
    }

    public void dbg(int n) {
        double[] val = new double[n + 1];
        double[] mx = new double[n + 1];

        for (int i = 1; i <= n; i++) {
            val[i] = (double) i / sum(i);
        }

        mx[n] = val[n];
        for (int i = n - 1; i >= 1; i--) {
            mx[i] = Math.min(mx[i + 1], val[i]);
        }

        for (int i = 1; i < n / 2; i++) {
            if (mx[i + 1] >= val[i]) {
                debug.debug("i", i);
            }
        }
    }
}
