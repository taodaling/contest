package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BBallsOfSteel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] balls = new int[2][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < 2; j++){
                balls[j][i] = in.ri();
            }
        }
        for(int i = 0; i < n; i++){
            boolean valid = true;
            for(int j = 0; j < n; j++){
                int d = 0;
                for(int t = 0; t < 2; t++){
                    d += Math.abs(balls[t][i] - balls[t][j]);
                }
                if(d > k){
                    valid = false;
                }
            }
            if(valid){
                out.println(1);
                return;
            }
        }
        out.println(-1);
    }
}
