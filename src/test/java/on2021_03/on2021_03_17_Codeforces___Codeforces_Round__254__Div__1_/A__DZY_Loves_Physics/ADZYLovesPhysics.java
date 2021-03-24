package on2021_03.on2021_03_17_Codeforces___Codeforces_Round__254__Div__1_.A__DZY_Loves_Physics;



import template.io.FastInput;
import template.io.FastOutput;

public class ADZYLovesPhysics {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.ri();
        }
        double ans = 0;
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            ans = Math.max(ans, (x[a] + x[b]) / (double) c);
        }
        out.println(ans);
    }
}
