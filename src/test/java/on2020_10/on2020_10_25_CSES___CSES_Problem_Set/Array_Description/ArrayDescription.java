package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Array_Description;



import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class ArrayDescription {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        int[] last = new int[m + 1];
        int[] next = new int[m + 1];
        int mod = (int) 1e9 + 7;
        Arrays.fill(last, 1);
        for (int i = 1; i <= n; i++) {
            Arrays.fill(next, 0);
            if (x[i - 1] != 0) {
                for (int j = 0; j <= m; j++) {
                    if (j != x[i - 1]) {
                        last[j] = 0;
                    }
                }
            }
            if (i == n) {
                break;
            }
            for (int j = 1; j <= m; j++) {
                for (int d = -1; d <= 1; d++) {
                    if (j + d >= 1 && j + d <= m) {
                        next[j + d] = DigitUtils.modplus(next[j + d], last[j], mod);
                    }
                }
            }
            int[] tmp = last;
            last = next;
            next = tmp;
        }
        long ans = 0;
        for(int i = 1; i <= m; i++){
            ans += last[i];
        }
        out.println(ans % mod);
    }
}
