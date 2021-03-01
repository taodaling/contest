package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EResearchRover {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e6, mod);
    Power power = new Power(mod);

    public long way(int x, int y) {
        return comb.combination(x + y, x);
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int s = in.ri();
        List<int[]> pos = new ArrayList<>(n + 2);
        boolean containBegin = false;
        boolean containEnd = false;
        for (int i = 0; i < k; i++) {
            int x = in.ri();
            int y = in.ri();
            if (x == 1 && y == 1) {
                containBegin = true;
            }
            if (x == n && y == m) {
                containEnd = true;
            }
            pos.add(new int[]{x, y});
        }
        if (!containBegin) {
            pos.add(new int[]{1, 1});
        }
        if (!containEnd && (n > 1 || m > 1)) {
            pos.add(new int[]{n, m});
        }
        int[][] posArray = pos.toArray(new int[0][]);
        Arrays.sort(posArray, Comparator.<int[]>comparingInt(x -> x[0]).thenComparing(x -> x[1]));
        int L = posArray.length;
        long[][] dp = new long[L][30];
        dp[0][0] = 1;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < 30; j++) {
                dp[i][j] = DigitUtils.modWithoutDivision(dp[i][j], mod);
            }
            for (int j = i + 1; j < L; j++) {
                if (posArray[j][0] < posArray[i][0] || posArray[j][1] < posArray[i][1]) {
                    continue;
                }
                long w = way(posArray[j][0] - posArray[i][0],
                        posArray[j][1] - posArray[i][1]);
                for (int t = 0; t < 30; t++) {
                    int to = Math.min(t + 1, 29);
                    dp[j][to] += w * dp[i][t] % mod;
                }
            }
        }
        debug.debug("dp", dp);
        int plus = 0;
        if (containBegin) {
            plus++;
        }
        if (containEnd && (n > 1 || m > 1)) {
            plus++;
        }
        long[] state = new long[30];
        for (int i = 0; i < 30; i++) {
            int to = Math.min(i + plus, 29);
            state[to] += dp[L - 1][i];
        }
        for (int i = 0; i < 30; i++) {
            state[i] = DigitUtils.modWithoutDivision(state[i], mod);
        }
        debug.debug("state", state);
        long exp = 0;
        int battery = s;
        for (int i = 0; i < 30; i++, battery = (battery + 1) / 2) {
            exp += state[i] * battery % mod;
        }
        exp %= mod;
        exp = exp * power.inverse((int) way(n - 1, m - 1)) % mod;
        out.println(exp);
    }
}
