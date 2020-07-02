package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class AbstractThinking {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long sub = choose(n + 1, 3);
        for (int i = 2; i < n; i++) {
            int pt = n - (i - 1);
            int way = i - 1;
            sub += way * choose(pt, 2);
        }

        for(int i = 1; i < n; i++){
            int left = i;
            int right = n - i;
            if(left > right){
                continue;
            }
            long contrib = (comb(left + 1, 2) - 1) * (comb(right + 1, 2) - 1);
            if(left < right) {
                contrib *= n;
            }else{
                contrib *= n / 2;
            }
            sub += contrib;
        }

        long total = comb(comb(n, 2), 3);

        long ans = total - sub;

        debug.debug("sub", sub);
        debug.debug("total", total);
        debug.debug("ans", ans);
        out.println(ans);
    }

    public long choose(int n, int k) {
        //x1 + y1 + ... + xk + yk <= n - 1
        //(2k + n - 1 - k \choose n - 1 - k)
        //x1,...,xk>=1
        long up = 2 * k + n - 1 - k;
        long bot = n - 1 - k;
        return comb(up, bot);
    }

    private long comb(long n, long m) {
        if (m > n || m < 0) {
            return 0;
        }
        return m == 0 ? 1 : (comb(n - 1, m - 1) * n / m);
    }
}
