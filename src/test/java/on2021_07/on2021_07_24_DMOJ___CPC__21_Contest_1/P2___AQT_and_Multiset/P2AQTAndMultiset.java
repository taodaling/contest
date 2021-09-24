package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P2___AQT_and_Multiset;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class P2AQTAndMultiset {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2 + 1;
        long x = 0;
        long[] a = in.rl(n);
        long[] b = in.rl(n);
        for (long y : a) {
            x ^= y;
        }
        for (long y : b) {
            x ^= y;
        }
        for (int i = 0; i < n; i++) {
            a[i] ^= x;
        }
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) {
                out.println(-1);
                return;
            }
        }
        out.println(x);
    }
}
