package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__225__Div__1_.D__Antimatter;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class DAntimatter {
    int lim = 10000;
    int[] a;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.ri(n);
        dac(0, n - 1);
        out.println(total % mod);
    }

    int mod = (int) 1e9 + 7;

    long[] prev = new long[lim + 1];
    long[] next = new long[lim + 1];
    long[] sum = new long[20000];
    long total = 0;

    public int dac(int l, int r) {
        if (l >= r) {
            return a[l];
        }
        int m = (l + r) / 2;
        int lSum = dac(l, m);
        int rSum = dac(m + 1, r);
        Arrays.fill(sum, 0, lSum * 2 + 1, 0);
        Arrays.fill(prev, 0, lSum + 1, 0);
        prev[0] = 1;
        int now = 0;
        for (int i = m; i >= l; i--) {
            now += a[i];
            Arrays.fill(next, 0, lSum + 1, 0);
            for (int j = 0; j <= lSum; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                next[j] += prev[j];
                next[j + a[i]] += prev[j];
            }
            for (int j = 0; j <= lSum; j++) {
                next[j] = DigitUtils.modWithoutDivision(next[j], mod);
            }

            for (int j = 0; j <= lSum; j++) {
                sum[lSum + j - (now - j)] += next[j];
            }

            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        for (int j = 0; j <= 2 * lSum; j++) {
            sum[j] %= mod;
        }

        Arrays.fill(prev, 0, rSum + 1, 0);
        prev[0] = 1;
        now = 0;
        for (int i = m + 1; i <= r; i++) {
            now += a[i];
            Arrays.fill(next, 0, rSum + 1, 0);
            for (int j = 0; j <= rSum; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                next[j] += prev[j];
                next[j + a[i]] += prev[j];
            }
            for (int j = 0; j <= rSum; j++) {
                next[j] = DigitUtils.modWithoutDivision(next[j], mod);
            }

            for (int j = 0; j <= rSum; j++) {
                if (next[j] != 0) {
                    int v = j - (now - j);
                    int opposite = -v;
                    if (opposite >= -lSum && opposite <= lSum) {
                        total += next[j] * sum[opposite + lSum] % mod;
                    }
                }
            }

            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        return lSum + rSum;
    }
}
