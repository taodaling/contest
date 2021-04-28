package on2021_04.on2021_04_23_Codeforces___Contest_2050_and_Codeforces_Round__718__Div__1___Div__2_.C__Fillomino_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CFillomino2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = in.ri(n);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            int v = p[i];
            int x = i;
            int y = i;
            mat[x][y] = p[i];
            v--;
            while (v > 0) {
                v--;
                if (y > 0 && mat[x][y - 1] == 0) {
                    y--;
                } else {
                    x++;
                }
                mat[x][y] = p[i];
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j <= i; j++){
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }
    }
}
