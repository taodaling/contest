package on2020_07.on2020_07_28_AtCoder___CODE_FESTIVAL_2017_Final.G___Mancala;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

import java.util.Arrays;

public class GMancala {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        int total = calcSum(n, k);
        int sub = calcSubtract(n, k);
        debug.debug("total", total);
        debug.debug("sub", sub);
        int ans = mod.subtract(total, sub);
        out.println(ans);
    }

    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    public int calcSum(int n, int k) {
        int sum = k * (k + 1) / 2;
        sum = mod.mul(sum, pow.pow(k + 1, n - 1));
        sum = mod.mul(sum, n);
        return sum;
    }

    public int calcSubtract(int n, int k) {
        int m = 8000;
        int[] last = new int[m];
        int[] cur = new int[m];
        last[0] = 1;
        for (int i = n; i >= 1; i--) {
            Arrays.fill(cur, 0);
            for (int j = 0; j < m; j++) {
                int way = last[j];
                for (int t = 0; t <= k; t++) {
                    int to = t > i ? j : j + (j + t) / i;
                    if (to >= m) {
                        continue;
                    }
                    cur[to] = mod.plus(cur[to], way);
                }
            }
            int[] tmp = last;
            last = cur;
            cur = tmp;
        }

        int ans = 0;
        for (int i = 0; i < m; i++) {
            ans = mod.plus(ans, mod.mul(i, last[i]));
        }
        debug.debug("last", last);
        return ans;
    }
}
