package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class EApolloVersusPan {
    int mod = (int) 1e9 + 7;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] x = new long[n];
        in.populate(x);
        int[] c = new int[60];
        for (int i = 0; i < 60; i++) {
            for (long t : x) {
                c[i] += Bits.get(t, i);
            }
        }
        long[] z = new long[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 60; j++) {
                int add = Bits.get(x[i], j) == 1 ? n : c[j];
                z[i] += (1L << j) % mod * add % mod;
            }
            z[i] %= mod;
        }

        debug.debug("c", c);
        debug.debug("z", z);
        long ans = 0;
        for (int i = 0; i < 60; i++) {
            long m = (1L << i) % mod;
            for (int j = 0; j < n; j++) {
                if(Bits.get(x[j], i) == 1) {
                    long contrib = c[i] * z[j] % mod * m % mod;
                    ans += contrib;
                }
            }
        }
        ans %= mod;
        out.println(ans);
    }
}
