package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class DMOPC20Contest4P5CyclicCypher {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int g = GCDs.gcd(n, k);
        if (n == g) {
            out.println(0);
            return;
        }
        int[] value = null;
        if (n / g % 2 == 0) {
            value = new int[n];
            for (int i = 0; i < g; i++) {
                int now = i;
                int last = 1;
                while (value[now] == 0) {
                    value[now] = last;
                    last *= -1;
                    now += k;
                    now %= n;
                }
            }
        }
        if (g % 4 == 0) {
            value = new int[n];
            for (int i = 0; i < k; i++) {
                value[i] = -1;
            }
            int last = 1;
            for (int i = k; i < n; i += 4, last = -last) {
                value[i] = last;
                value[i + 1] = -last;
                value[i + 2] = -last;
                value[i + 3] = last;
            }
        }
        if (value == null) {
            out.println(0);
            return;
        }
        for (int i = 0; i < n; i++) {
            out.append(value[i]);
            if (i + 1 < n) {
                out.append(' ');
            }
        }
        out.println();
    }
}
