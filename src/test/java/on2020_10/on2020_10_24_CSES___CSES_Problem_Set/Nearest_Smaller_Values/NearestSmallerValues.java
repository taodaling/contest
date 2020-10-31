package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Nearest_Smaller_Values;



import template.io.FastInput;

import java.io.PrintWriter;

public class NearestSmallerValues {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] x = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            x[i] = in.readInt();
        }
        int[] pre = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            pre[j] = j - 1;
            while (x[pre[j]] >= x[j]) {
                pre[j] = pre[pre[j]];
            }
            out.println(pre[j]);
        }
    }
}
