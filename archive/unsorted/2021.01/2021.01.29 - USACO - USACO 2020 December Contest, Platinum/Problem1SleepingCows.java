package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class Problem1SleepingCows {
    int mod = (int) 1e9 + 7;

    void normalize(long[][] x) {
        int n = x.length;
        int m = x[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                x[i][j] = DigitUtils.modWithoutDivision(x[i][j], mod);
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        long[][] prev = new long[2][n + 1];
        long[][] next = new long[2][n + 1];
        prev[0][0] = 1;
        int L = 0;
        for (int x : b) {
            while (L < n && a[L] <= x) {
                SequenceUtils.deepFill(next, 0L);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j <= n; j++) {
                        //not terminate
                        if (i == 1) {
                            next[i][j] += prev[i][j];
                        }
                        if (j + 1 <= n) {
                            next[i][j + 1] += prev[i][j];
                        }
                        //terminate
                        if (i == 0) {
                            next[1][j] += prev[i][j];
                        }
                    }
                }
                normalize(next);
                long[][] tmp = prev;
                prev = next;
                next = tmp;

                L++;
            }
            SequenceUtils.deepFill(next, 0L);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= n; j++) {
                    if (i == 0) {
                        next[i][j] += prev[i][j];
                    }
                    if (j > 0) {
                        next[i][j - 1] += prev[i][j] * j % mod;
                    }
                }
            }
            normalize(next);
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = prev[0][0] + prev[1][0];
        ans %= mod;
        out.println(ans);
    }
}
