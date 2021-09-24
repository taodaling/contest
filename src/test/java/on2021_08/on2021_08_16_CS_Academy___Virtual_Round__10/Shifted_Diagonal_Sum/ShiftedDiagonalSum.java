package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Shifted_Diagonal_Sum;



import template.io.FastInput;
import template.io.FastOutput;

public class ShiftedDiagonalSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri();
            }
        }
        int best = (int)-1e9;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    int mx = x + i;
                    int my = y + i;
                    if(mx >= n){
                        mx -= n;
                    }
                    if(my >= n){
                        my -= n;
                    }
                    sum += mat[mx][my];
                }
                best = Math.max(best, sum);
            }
        }
        out.println(best);
    }
}
