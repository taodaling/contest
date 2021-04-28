package on2021_04.on2021_04_23_Codeforces___Contest_2050_and_Codeforces_Round__718__Div__1___Div__2_.B__Morning_Jogging;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class BMorningJogging {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
            Randomized.shuffle(mat[i]);
            Arrays.sort(mat[i]);
        }
        int[][] range = new int[n][2];
        for (int i = 0; i < n; i++) {
            range[i][0] = 0;
            range[i][1] = m - 1;
        }
        int[][] choose = new int[n][m];
        for (int i = 0; i < m; i++) {
            int minIndex = 0;
            for (int j = 0; j < n; j++) {
                if (mat[minIndex][range[minIndex][0]] > mat[j][range[j][0]]) {
                    minIndex = j;
                }
            }
            choose[minIndex][i] = mat[minIndex][range[minIndex][0]];
            range[minIndex][0]++;
            for(int j = 0; j < n; j++){
                if(j == minIndex){
                    continue;
                }
                choose[j][i] = mat[j][range[j][1]];
                range[j][1]--;
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(choose[i][j]).append(' ');
            }
            out.println();
        }
    }
}
