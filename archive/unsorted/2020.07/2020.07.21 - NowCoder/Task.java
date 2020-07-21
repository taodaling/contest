package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int l = in.readInt();
        int r = in.readInt();

        long[] rSum = solve(r);
        long[] lSum = solve(l - 1);
        for(int i = 1; i <= 9; i++){
            out.println(rSum[i] - lSum[i]);
        }
    }

    public void add(long[] cnts, int l, int r, long time) {
        for (int i = 1; i <= 9; i++) {
            long sum = 0;
            for (long base = 1; base <= r; base *= 10) {
                if (i * base > r) {
                    break;
                }
                if ((i + 1) * base <= l) {
                    continue;
                }
                long left = Math.max(l, i * base);
                long right = Math.min(r, (i + 1) * base - 1);
                sum += right - left + 1;
            }
            cnts[i] += sum * time;
        }
    }

    public long[] solve(int n) {
        long[] ans = new long[10];
        for (int i = 1, r; i <= n; i = r + 1) {
            r = n / (n / i);
            add(ans, i, r, n / i);
        }
        return ans;
    }
}
