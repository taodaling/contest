package on2021_04.on2021_04_24_Codeforces___Contest_2050_and_Codeforces_Round__718__Div__1___Div__2_.E__Group_Photo;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

import java.util.Arrays;

public class EGroupPhoto {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        if (n == 1) {
            out.println(1);
            return;
        }
        ps = new LongPreSum(i -> a[i], n);
        for(int i = 0; i < 2; i++){
            delta[i] = new LongPreSum(n);
        }
        long ans = 0;
        //all one side
//        ans++;
        //only one
        //for x
//        if (n > 2 && ps.intervalSum(0, n - 1) < a[n - 2] * 2) {
//            ans++;
//        }
//        if (n > 2 && ps.intervalSum(0, n - 1) > a[1] * 2) {
//            ans++;
//        }
        //different side
        for (int i = -1; i < n; i++) {
            if (ps.intervalSum(0, i) > ps.intervalSum(i + 1, n - 1)) {
                ans++;
            }
        }
        debug.debug("ans", ans);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int[] data = Arrays.copyOfRange(a, i, n - j);
                long y = i * a[0];
                long x = j * a[n - 1];
                long contrib = solve(data, x, y);
                ans += contrib;
                debug.debug("i", i);
                debug.debug("j", j);
                debug.debug("contrib", contrib);
            }
        }

        debug.debug("ans", ans);
        ans %= mod;
        out.println(ans);
    }

    Debug debug = new Debug(false);
    LongPreSum ps;
    LongPreSum[] delta = new LongPreSum[2];

    public long solve(int[] seq, long x, long y) {
        if (seq.length <= 1) {
            return 0;
        }
        int n = seq.length;
        ps.populate(i -> seq[i], n);
        for(int i = 0; i < 2; i++){
            int finalI = i;
            delta[i].populate(j -> (j & 1) != finalI ? 0 : seq[j] - seq[j + 1], n - 1);
        }
        long sum = x;
        long ans = 0;
        for (int i = 0; i < n - 1; i++) {
            sum += seq[i];
            int l = i + 1;
            int r = n - 1;
            if (r % 2 != l % 2) {
                r--;
            }
            int bit = l & 1;
            l >>= 1;
            r >>= 1;
            while (l < r) {
                int mid = ((r + l + 1) / 2) * 2 + bit;
                //check
                long right = y + ps.post(mid) + delta[bit].intervalSum(i + 1, mid - 1);
                if (sum < right) {
                    //ok
                    l = mid >> 1;
                } else {
                    r = (mid >> 1) - 1;
                }
            }
            l = l * 2 + bit;
            r = r * 2 + bit;
            int mid = r;
            long right = y + ps.post(mid) + delta[bit].intervalSum(i + 1, mid - 1);
            if (sum < right) {
                //ok
                int choice = (l - i - 1) / 2 + 1;
                ans += choice;
            }
        }
        return ans;
    }
}
