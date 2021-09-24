package on2021_08.on2021_08_12_CS_Academy___Virtual_Beta_Round__6.Perm_Matrix;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.DifferentColorPerfectMatching;

public class PermMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri() - 1;
            }
        }
        DifferentColorPerfectMatching pm = new DifferentColorPerfectMatching(m, (int) 1e6);
        int[][] ans = new int[n][m];
        ans[0] = mat[0];
        int[] perm = new int[m];
        for (int i = 1; i < n; i++) {
            boolean ok = pm.solve(ans[i - 1], mat[i], perm);
            if(!ok){
                out.println(-1);
                return;
            }
            for (int j = 0; j < m; j++) {
                ans[i][j] = mat[i][perm[j]];
            }
        }

        for(int[] row : ans){
            for(int x : row){
                out.append(x + 1).append(' ');
            }
            out.println();
        }


    }
}
