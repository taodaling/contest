package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Brave_Seekers_of_Unicorns;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.math.DigitUtils;

import java.util.Arrays;

public class BraveSeekersofUnicorns {
    int mod = 998244353;
    CachedPow2 cp = new CachedPow2(2, mod);

    public long subset(long n) {
        return cp.pow(n);
    }

    public long nonEmptySubset(long n) {
        return cp.pow(n) - 1;
    }

    int n;

    public int count(int l, int r) {
        r = Math.min(r, n);
        if (l > r) {
            return 0;
        }
        return r - l + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        long[] prev = new long[20];
        long[] next = new long[20];
        for (int i = 0; i < 20; i++) {
            Arrays.fill(next, 0);
            int L = 1 << i;
            int R = (1 << (i + 1)) - 1;
            R = Math.min(R, n);
            next[i] += nonEmptySubset(count(L, R));
            for (int j = 0; j < 20; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                //transfer
                next[j] += prev[j];
                long way = nonEmptySubset(count(L, R));
                for (int k = L; k <= R; k++) {
                    if (Bits.get(k, j) == 0) {
                        continue;
                    }
                    way -= subset(count(k + 1, R));
                }
                next[i] += prev[j] * (way % mod) % mod;
            }
            for (int j = 0; j < 20; j++) {
                next[j] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = 0;
        for(int i = 0; i < 20; i++){
            ans += prev[i];
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
