package template.string;

import java.util.function.IntUnaryOperator;

public class ZAlgorithm {
    /**
     * Provide sequence : s(0), s(1), ... , s(n - 1), calculate their z function.<br>
     * z[i]: The longest substring start at i which is also a prefix of s
     */
    public static void generate(int[] z, IntUnaryOperator s) {
        int n = z.length;
        if (n == 0) {
            return;
        }

        int l = 0;
        int r = -1;
        z[0] = n;
        for (int i = 1; i < n; i++) {
            if (r < i) {
                l = r = i;
            } else {
                int t = i - l;
                int k = r - i + 1;
                if (z[t] < k) {
                    z[i] = z[t];
                    continue;
                }
                l = i;
                r++;
            }
            while (r < n && s.applyAsInt(r - l) == s.applyAsInt(r)) {
                r++;
            }
            r--;
            z[i] = r - l + 1;
        }
    }
}
