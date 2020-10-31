package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Trailing_Zeros;



import template.io.FastInput;

import java.io.PrintWriter;

public class TrailingZeros {
    public long log(long n, int x) {
        long ans = 0;
        while (n >= x) {
            n /= x;
            ans += n;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long n = in.readInt();
        long ans = Math.min(log(n, 2), log(n, 5));
        out.println(ans);
    }
}
