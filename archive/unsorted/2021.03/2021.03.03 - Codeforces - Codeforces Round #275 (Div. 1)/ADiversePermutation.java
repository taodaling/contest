package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ADiversePermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int l = 1;
        int r = n;
        boolean left = true;
        for (int i = 0; i < k - 1; i++) {
            if (left) {
                out.append(l++).append(' ');
            } else {
                out.append(r--).append(' ');
            }
            left = !left;
        }
        while (l <= r) {
            if (left) {
                out.append(l++).append(' ');
            } else {
                out.append(r--).append(' ');
            }
        }
    }
}
