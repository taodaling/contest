package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BNeighborGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                mat[i][j] = in.readInt();
            }
        }

        int[][] finals = new int[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                finals[i][j] = 0;
                if(i > 0){
                    finals[i][j]++;
                }
                if(j > 0){
                    finals[i][j]++;
                }
                if(i + 1 < n){
                    finals[i][j]++;
                }
                if(j + 1 < m){
                    finals[i][j]++;
                }
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(mat[i][j] > finals[i][j]){
                    out.println("NO");
                    return;
                }
            }
        }

        out.println("YES");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(finals[i][j]).append(' ');
            }
            out.println();
        }
    }
}
