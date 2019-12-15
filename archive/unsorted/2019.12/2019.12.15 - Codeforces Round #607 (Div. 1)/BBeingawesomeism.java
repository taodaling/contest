package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BBeingawesomeism {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() == 'A' ? 1 : 0;
            }
        }

        int[] row = new int[n];
        int[] col = new int[m];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                row[i] += mat[i][j];
                col[j] += mat[i][j];
            }
            sum += row[i];
        }

        if (sum == 0) {
            out.println("MORTAL");
            return;
        }
        if(sum == n * m){
            out.println(0);
            return;
        }

        int min = 4;
        if (mat[0][0] + mat[n - 1][0] +
                mat[0][m - 1] + mat[n - 1][m - 1] > 0) {
            min = Math.min(min, 2);
        }
        if (row[0] == m || row[n - 1] == m ||
                col[0] == n || col[m - 1] == n) {
            min = Math.min(min, 1);
        }
        for(int i = 0; i < n; i++){
            if(row[i] == m){
                min = Math.min(min, 2);
            }
        }
        for(int i = 0; i < m; i++){
            if(col[i] == n){
                min = Math.min(min, 2);
            }
        }
        if(row[0] + row[n - 1] + col[0] + col[m - 1] > 0){
            min = Math.min(min, 3);
        }

        out.println(min);
    }
}
