package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskC {
    DigitUtils.Log2 log2 = new DigitUtils.Log2();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        if (Integer.bitCount(a + 1) != 1) {
            out.println((1 << log2.ceilLog(a + 1)) - 1);
            return;
        }
        int max = 1;
        for (int i = 2; i * i <= a; i++) {
            if (a % i != 0) {
                continue;
            }
            max = Math.max(max, a / i);
        }
        out.println(max);
    }
}
