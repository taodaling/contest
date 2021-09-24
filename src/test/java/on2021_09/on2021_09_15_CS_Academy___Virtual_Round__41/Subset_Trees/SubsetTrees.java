package on2021_09.on2021_09_15_CS_Academy___Virtual_Round__41.Subset_Trees;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class SubsetTrees {
    int mod = (int) 1e9 + 7;

    public void update(long[] data, Int l, Int r, long x) {
        if (l == null || r != null && l.id >= r.id) {
            return;
        }
        data[l.id] += x;
        if (r != null) {
            data[r.id] -= x;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Int[] ints = new Int[n];
        for (int i = 0; i < n; i++) {
            ints[i] = new Int(in.ri(), in.ri());
        }
        Arrays.sort(ints, Comparator.<Int>comparingInt(x -> x.l)
                .thenComparingInt(x -> -(x.r - x.l)));
        for (int i = 0; i < n; i++) {
            ints[i].id = i;
        }
        Int[] reg = new Int[(int) 1e4];
        for (Int x : ints) {
            for (int i = x.l; i >= 0 && reg[i] == null; i--) {
                reg[i] = x;
            }
        }
        debug.debug("reg", reg);
        long[][] dp = new long[n][n];
        long[][] upd = new long[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i]++;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j > 0) {
                    upd[i][j] += upd[i][j - 1];
                    upd[i][j] %= mod;
                }
                dp[i][j] += upd[i][j];
                dp[i][j] %= mod;
                if (dp[i][j] == 0) {
                    continue;
                }
                if (ints[i].r < ints[j].r) {
                    dp[j][i] += dp[i][j];
                    dp[i][j] = 0;
                    continue;
                }
                if (i == j && i + 1 < n) {
                    update(upd[i], ints[i + 1], reg[ints[i].r + 1], dp[i][j]);
                } else {
                    int start = ints[j].r + 1;
                    update(upd[i], reg[start], reg[ints[i].r + 1], dp[i][j]);
                }
            }
        }

        debug.debug("ints", ints);
        debug.debug("dp", dp);
        debug.debug("upd", upd);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans += dp[i][j];
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}

class Int {
    int l;
    int r;

    public Int(int l, int r) {
        this.l = l;
        this.r = r;
    }

    int id;

    @Override
    public String toString() {
        return "Int{" +
                "l=" + l +
                ", r=" + r +
                ", id=" + id +
                '}';
    }
}