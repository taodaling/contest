package on2021_03.on2021_03_19_Codeforces___Codeforces_Round__245__Div__1_.B__Working_out;



import template.io.FastInput;
import template.io.FastOutput;

public class BWorkingOut {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }
        int[][] fromLT = new int[n][m];
        int[][] fromLB = new int[n][m];
        int[][] fromRT = new int[n][m];
        int[][] fromRB = new int[n][m];

        fromLT[0][0] = mat[0][0];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                fromLT[i][j] = mat[i][j];
                if(i > 0){
                    fromLT[i][j] = Math.max(fromLT[i][j], fromLT[i - 1][j] + mat[i][j]);
                }
                if(j > 0){
                    fromLT[i][j] = Math.max(fromLT[i][j], fromLT[i][j - 1] + mat[i][j]);
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                fromRB[i][j] = mat[i][j];
                if(i + 1 < n){
                    fromRB[i][j] = Math.max(fromRB[i][j], fromRB[i + 1][j] + mat[i][j]);
                }
                if(j + 1 < m){
                    fromRB[i][j] = Math.max(fromRB[i][j], fromRB[i][j + 1] + mat[i][j]);
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                fromLB[i][j] = mat[i][j];
                if(i + 1 < n){
                    fromLB[i][j] = Math.max(fromLB[i][j], fromLB[i + 1][j] + mat[i][j]);
                }
                if(j > 0){
                    fromLB[i][j] = Math.max(fromLB[i][j], fromLB[i][j - 1] + mat[i][j]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = m - 1; j >= 0; j--) {
                fromRT[i][j] = mat[i][j];
                if(i > 0){
                    fromRT[i][j] = Math.max(fromRT[i][j], fromRT[i - 1][j] + mat[i][j]);
                }
                if(j + 1 < m){
                    fromRT[i][j] = Math.max(fromRT[i][j], fromRT[i][j + 1] + mat[i][j]);
                }
            }
        }

        int ans = 0;
        for(int i = 1; i < n - 1; i++){
            for(int j = 1; j < m - 1; j++){
                //meet point
                int cand1 = fromLT[i][j - 1] + fromRB[i][j + 1] + fromLB[i + 1][j] + fromRT[i - 1][j];
                int cand2 = fromLT[i - 1][j] + fromRB[i + 1][j] + fromLB[i][j - 1] + fromRT[i][j + 1];
                ans = Math.max(ans, cand1);
                ans = Math.max(ans, cand2);
            }
        }
        out.println(ans);
    }
}
