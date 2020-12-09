package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class GreatestGreatestCommonDivisor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int threshold = (int) 1e6;
        int[] cnts = new int[threshold + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.ri()]++;
        }
        for (int i = threshold; i >= 1; i--) {
            int num = 0;
            for (int j = i; j <= threshold; j += i) {
                num += cnts[j];
            }
            if (num >= 2) {
                out.println(i);
                return;
            }
        }
        assert false;
    }
}
