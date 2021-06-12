package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.D___1_or_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class D1Or2 {
    long inf = (long)1e18;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        Randomized.shuffle(a);
        Arrays.sort(a);

        long L = inf;
        long R = -inf;
        int l = 0;
        int r = n - 1;
        while (l <= r && a[l] < 0 && a[r] > 0) {
            long merge = a[r] + a[l];
            L = Math.min(L, merge);
            R = Math.max(R, merge);
            l++;
            r--;
        }
        if (l > r) {
            out.println(R - L);
            return;
        }
        long ans = calc(Arrays.copyOfRange(a, l, r + 1), L, R);
        out.println(ans);
    }

    public long calc(long[] a, long L, long R) {
        int n = a.length;
        if (Long.signum(a[0]) * Long.signum(a[n - 1]) >= 0) {
            if (a[0] < 0) {
                for (int i = 0; i < n; i++) {
                    a[i] = -a[i];
                }
                L = -L;
                R = -R;
                long tmp = L;
                L = R;
                R = tmp;
                SequenceUtils.reverse(a);
            }
        }

        long ans = Math.max(R, a[n - 1]) - Math.min(L, a[0]);
        for (int i = 0; i <= n; i += 2) {
            long max = a[n - 1];
            long min = i == n ? a[0] + a[n - 1] : a[i];
            max = Math.max(max, R);
            min = Math.min(min, L);
            int l = 0;
            int r = i - 1;
            while (l < r) {
                max = Math.max(max, a[l] + a[r]);
                min = Math.min(min, a[l] + a[r]);
                l++;
                r--;
            }
            ans = Math.min(ans, max - min);
        }
        return ans;
    }
}
