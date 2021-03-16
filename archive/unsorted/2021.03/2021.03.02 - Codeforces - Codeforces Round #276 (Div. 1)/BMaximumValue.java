package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BMaximumValue {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 2e6;
        boolean[] occur = new boolean[limit + 1];
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            occur[in.ri()] = true;
        }
        int[] left = new int[limit + 1];
        for (int i = 1; i <= limit; i++) {
            left[i] = left[i - 1];
            if (occur[i]) {
                left[i] = i;
            }
        }
        int ans = 0;
        for (int i = 1; i <= limit; i++) {
            if (!occur[i]) {
                continue;
            }
            for (int j = i + i; j <= limit; j += i) {
                ans = Math.max(ans, left[j - 1] - (j - i));
            }
        }
        out.println(ans);
    }
}
