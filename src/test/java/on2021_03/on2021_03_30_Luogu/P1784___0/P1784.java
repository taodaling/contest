package on2021_03.on2021_03_30_Luogu.P1784___0;





import template.io.FastInput;
import template.io.FastOutput;
import template.problem.SudokuProblem;

public class P1784 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[][] mat = new int[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                mat[i][j] = in.ri();
            }
        }
        int[][] ans = SudokuProblem.solve(mat);
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                out.append(ans[i][j]).append(' ');
            }
            out.println();
        }
    }
}
