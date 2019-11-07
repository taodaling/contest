package on2019_11.on2019_11_07_AtCoder_Grand_Contest_027.B___Garbage_Collector;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.PreSum;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long x = in.readInt();

        long[] coords = new long[n];
        for (int i = 0; i < n; i++) {
            coords[i] = in.readInt();
        }

        long ans = Long.MAX_VALUE;
        PreSum ps = new PreSum(coords);
        for (int i = n - 1; i >= 0; i--) {
            int k = n - i;
            boolean valid = true;
            long localAns = k * x + 5 * ps.intervalSum(i, n - 1);
            int pick = 1;
            for (int j = i - 1; j >= 0; j -= k, pick++) {
                int r = j;
                int l = j - k + 1;
                l = Math.max(l, 0);
                long sum = ps.intervalSum(l, r);

                valid = valid && !DigitUtils.isMultiplicationOverflow(sum, (2 * pick + 3), Long.MAX_VALUE);
                valid = valid && !DigitUtils.isPlusOverflow(localAns, sum * (2 * pick + 3));
                localAns += sum * (2 * pick + 3);
            }

            if (valid) {
                ans = Math.min(localAns, ans);
            }
        }

        out.println(ans + n * x);
    }
}
