package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASumOfOddIntegers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readInt();
        long sum = (k - 1) * k + k;
        if (n - sum >= 0 && (n - sum) % 2 == 0) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
