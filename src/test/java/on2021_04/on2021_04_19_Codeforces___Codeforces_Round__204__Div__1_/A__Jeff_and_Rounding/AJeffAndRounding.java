package on2021_04.on2021_04_19_Codeforces___Codeforces_Round__204__Div__1_.A__Jeff_and_Rounding;



import template.io.FastInput;
import template.io.FastOutput;

public class AJeffAndRounding {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n * 2];
        for (int i = 0; i < n * 2; i++) {
            a[i] = (int) Math.round(in.rd() * 1000);
        }
        int[] cnt = new int[2];
        long sum = 0;
        for (int x : a) {
            sum += x % 1000;
            if (x % 1000 == 0) {
                cnt[0]++;
            } else {
                cnt[1]++;
            }
        }
        long best = (long) 1e18;
        for (int i = 0; i <= cnt[1]; i++) {
            int j = n - i;
            if (j < 0 || j > cnt[0]) {
                continue;
            }
            if (Math.abs(best - sum) > Math.abs(i * 1000 - sum)) {
                best = i * 1000;
            }
        }
        out.printf("%.3f", Math.abs(best - sum) / 1000d);
    }
}
