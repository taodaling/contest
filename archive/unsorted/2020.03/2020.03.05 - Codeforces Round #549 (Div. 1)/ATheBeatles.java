package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

public class ATheBeatles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readInt();
        long k = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        long[] possible = new long[4];
        possible[0] = DigitUtils.mod(b - a, n * k);
        possible[1] = DigitUtils.mod(-b - a, n * k);
        possible[2] = DigitUtils.mod(b + a, n * k);
        possible[3] = DigitUtils.mod(-b + a, n * k);

        long[] r = new long[]{(long)1e18, 0};
        for(long t : possible){
            long[] rd = round(t, n, k);
            r[0] = Math.min(r[0], rd[0]);
            r[1] = Math.max(r[1], rd[1]);
        }

        out.println(r[0]);
        out.println(r[1]);
    }

    public long[] round(long t, long n, long k) {
        long nk = n * k;
        long[] ans = new long[]{nk, 1};
        for (int i = 0; i < n; i++) {
            long g = GCDs.gcd(i * k + t, nk);
            ans[0] = Math.min(g, ans[0]);
            ans[1] = Math.max(g, ans[1]);
        }
        return new long[]{nk / ans[1], nk / ans[0]};
    }
}
