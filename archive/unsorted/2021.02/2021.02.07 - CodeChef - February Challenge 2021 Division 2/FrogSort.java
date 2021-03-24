package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class FrogSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] w = in.ri(n);
        long[] l = in.rl(n);
        long last = -1;
        int[] inv = new int[n + 1];
        for (int i = 0; i < n; i++) {
            inv[w[i]] = i;
        }
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            long pos = inv[i];
            if (pos <= last) {
                long jump = DigitUtils.ceilDiv(last + 1 - pos, l[inv[i]]);
                ans += jump;
                pos += jump * l[inv[i]];
            }
            last = pos;
        }
        out.println(ans);
    }
}
