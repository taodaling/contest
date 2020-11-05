package on2020_11.on2020_11_05_CSES___CSES_Problem_Set.Knight_s_Tour;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.KnightTour;

public class KnightsTour {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int c = in.readInt() - 1;
        int r = in.readInt() - 1;
        KnightTour kt = new KnightTour(100, 100, r, c);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                out.append(kt.mat[i][j] + 1).append(' ');
            }
            out.println();
        }
    }
}
