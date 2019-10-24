package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

public class TaskD {
    long[][] f;
    int n;
    int[] nums;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        nums = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            boolean positive = true;
            if (i > 1) {
                positive = in.readChar() == '+';
            }
            nums[i] = in.readInt();
            if (!positive) {
                nums[i] = -nums[i];
            }
        }

        f = new long[2][n + 1];
        ArrayUtils.deepFill(f, -1L);

        long ans = f(1, 0);
        out.println(ans);
    }

    public long f(int i, int j) {
        if (i > n) {
            return 0;
        }
        if (f[j][i] == -1) {
            if (j == 0) {
                if (nums[j] < 0) {
                    
                }

            }
        }
        return f[j][i];
    }

}
