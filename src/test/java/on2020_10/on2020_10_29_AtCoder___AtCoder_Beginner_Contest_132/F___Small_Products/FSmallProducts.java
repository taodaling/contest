package on2020_10.on2020_10_29_AtCoder___AtCoder_Beginner_Contest_132.F___Small_Products;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class FSmallProducts {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int mod = (int) 1e9 + 7;
        List<Interval> list = new ArrayList<>();
        for (int i = 1, r; i <= n; i = r + 1) {
            r = n / (n / i);
            list.add(new Interval(i, r, n / i, list.size()));
        }
        Interval[] intervals = list.toArray(new Interval[0]);
        int m = intervals.length;
        long[] prev = new long[m];
        long[] next = new long[m];
        prev[0] = 1;
        for (int i = 0; i < k; i++) {
            long sum = 0;
            for (int j = 0; j < m; j++) {
                sum += prev[j];
                next[j] = 0;
            }
            sum %= mod;
            int r = m - 1;
            for (int j = 0; j < m; j++) {
                while (r >= 0 && intervals[r].div < intervals[j].r) {
                    sum = DigitUtils.modsub(sum, prev[r], mod);
                    r--;
                }
                next[j] = intervals[j].len * sum % mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = 0;
        for(int i = 0; i < m; i++){
            ans += prev[i];
        }
        ans %= mod;
        out.println(ans);
    }
}

class Interval {
    int l;
    int r;
    int div;
    int id;
    int len;

    public Interval(int l, int r, int div, int id) {
        this.l = l;
        this.r = r;
        this.div = div;
        this.id = id;
        len = r - l + 1;
    }
}