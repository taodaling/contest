package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AABC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int K = in.ri();
        long ans = 0;
        for (int i = 1; i <= K; i++) {
            for (int j = 1; j * i <= K; j++) {
                ans += K / i / j;
            }
        }
        out.println(ans);
    }
}
