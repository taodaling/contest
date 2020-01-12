package template.string;

import java.util.function.IntUnaryOperator;

public class ZAlgorithm implements IntUnaryOperator {
    private int[] z;

    /**
     * Provide sequence : s(0), s(1), ... , s(n - 1), calculate their z function
     */
    public ZAlgorithm(int n, IntUnaryOperator s) {
        if(n == 0){
            return;
        }

        int l = 0;
        int r = -1;
        z = new int[n];
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

    @Override
    public int applyAsInt(int operand) {
        return z[operand];
    }
}
