package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class TaskH {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        char[][] mat = new char[h][w];
        for(int i = 0; i < h; i++){
            in.readString(mat[i], 0);
        }
        int[][] dp = new int[h][w];
        Modular mod = new Modular(1e9 + 7);
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                dp[i][j] = 0;
                if(i == 0 && j == 0){
                    dp[i][j] = 1;
                }
                if(i > 0){
                    dp[i][j] = mod.plus(dp[i][j], dp[i - 1][j]);
                }
                if(j > 0){
                    dp[i][j] = mod.plus(dp[i][j], dp[i][j - 1]);
                }
                if(mat[i][j] == '#'){
                    dp[i][j] = 0;
                }
            }
        }

        out.println(dp[h - 1][w - 1]);
    }
}
