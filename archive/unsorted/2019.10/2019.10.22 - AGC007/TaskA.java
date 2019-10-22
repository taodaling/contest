package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int[][] mat = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                mat[i][j] = in.readChar() == '#' ? 1 : 0;
            }
        }

        int last = 0;
        for (int i = 0; i < h; i++) {
            int j;
            for(j = 0; j < last; j++){
                if(mat[i][j] == 1){
                    impossible(out);
                    return;
                }
            }
            while(j + 1 < w && mat[i][j + 1] == 1){
                j++;
            }
            if(j < last){
                impossible(out);
                return;
            }
            last = j;
            for(int k = j + 1; k < w; k++){
                if(mat[i][k] == 1){
                    impossible(out);
                    return;
                }
            }
            if(i == h - 1 && last != w - 1){
                impossible(out);
                return;
            }
        }

        possible(out);
    }

    public void possible(FastOutput out){
        out.println("Possible");
    }

    public void impossible(FastOutput out){
        out.println("Impossible");
    }
}
