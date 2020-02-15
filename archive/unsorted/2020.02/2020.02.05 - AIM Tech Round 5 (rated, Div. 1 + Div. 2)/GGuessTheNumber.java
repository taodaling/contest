package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongList;
import template.utils.SequenceUtils;

import java.math.BigDecimal;

public class GGuessTheNumber {
    FastInput in;
    FastOutput out;
    long M = 10004205361450474L;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        try {
            solve();
        } catch (RuntimeException e) {
        }
    }

    long[][] dp = new long[10001][6];

    {
        SequenceUtils.deepFill(dp, -1L);
    }

    public void solve() {
        long l = 1;
        LongList list = new LongList(10000);
        System.err.println("dp(1,5)=" + dp(1, 5));

        for (int q = 5; q >= 1; q--) {
            long len = 0;
            list.clear();
            for (int i = 0; i < l && i < 10000; i++) {
                len += dp(l + len + i, q - 1);
                list.add(l + len + i);
            }
            while(list.tail() > M){
                list.pop();
            }
            int ans = ask(list);
            if(ans == 0){
                continue;
            }
            l = list.get(ans - 1) + 1;
        }
    }

    public long dp(long l, int q) {
        return dp((int) Math.min(l, 10000), q);
    }

    public long dp(int l, int q) {
        if (q == 0) {
            return 0;
        }
        if (dp[l][q] == -1) {
            long len = 0;
            for (int i = 0; i <= l; i++) {
                if (l + len >= 10000) {
                    long a = dp(l + len + i, q - 1);
                    long b = l - i + 1;
                    len += DigitUtils.isMultiplicationOverflow(a, b, M) ? M : a * b;
                    break;
                }
                len += dp(l + len + i, q - 1);
            }
            len += l;
            dp[l][q] = Math.min(M, len);
        }
        return dp[l][q];
    }

    private int ask(LongList points) {
        out.println(points.size());
        for (int i = 0; i < points.size(); i++) {
            out.append(points.get(i)).append(' ');
        }
        out.println().flush();
        int ans = in.readInt();
        if (ans < 0) {
            throw new RuntimeException();
        }
        return ans;
    }

}