package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.A__Unusual_Product;



import template.io.FastInput;
import template.io.FastOutput;

public class AUnusualProduct {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] mat = new int[n][n];
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri();
            }
            ans ^= mat[i][i];
        }
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1 || t == 2) {
                int index = in.ri() - 1;
                ans ^= 1;
            }else{
                out.append(ans);
            }
        }
    }
}
