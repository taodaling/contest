package on2021_10.on2021_10_19_AtCoder___Daiwa_Securities_Co__Ltd__Programming_Contest_2021_AtCoder_Regular_Contest_128_.D___Neq_Neq;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DNeqNeq {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] same = new int[n];
        for (int i = 2; i < n; i++) {
            if (a[i] == a[i - 2]) {
                same[i] = 1;
            }
        }
        long ans = 1;
        for (int i = 1; i < n - 1; i++) {
            if (a[i] == a[i - 1] || a[i] == a[i + 1]) {
                continue;
            }
            int l = i;
            int r = i;
            while (r + 2 < n && a[r + 1] != a[r] && a[r + 1] != a[r + 2]) {
                r++;
            }
            i = r;
            int[] subarray = Arrays.copyOfRange(same, l + 1, r + 2);
            long way = solve(subarray);
            ans = ans * way % mod;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    public long solve(int[] data) {
        long[][] prev = new long[3][2];
        long[][] next = new long[3][2];
        prev[0][0] = 1;
        for (int x : data) {
            SequenceUtils.deepFill(next, 0L);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {
                    if (prev[i][j] == 0) {
                        continue;
                    }
                    //pick
                    {
                        int ni = Math.min(i + 1, 2);
                        int nj = x == 0 ? 1 : j;
                        next[ni][nj] += prev[i][j];
                    }
                    //not pick
                    if (i <= 1 || j == 1) {
                        int ni = 0;
                        int nj = 0;
                        next[ni][nj] += prev[i][j];
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {
                    next[i][j] %= mod;
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        long sum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (i <= 1 || j == 1) {
                    sum += prev[i][j];
                }
            }
        }
        sum %= mod;
        return sum;
    }
}
