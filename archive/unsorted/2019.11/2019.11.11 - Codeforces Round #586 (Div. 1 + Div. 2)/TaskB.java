package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        double ab = mat[0][1];
        double bc = mat[1][2];
        double aDivC = ab / bc;
        double ac = mat[0][2];
        double a = Math.sqrt(ac) * Math.sqrt(aDivC);

        int[] vals = new int[n];
        vals[0] = (int) (a + 0.5);
        for(int i = 1; i < n; i++){
            vals[i] = mat[0][i] / vals[0];
        }

        for(int i = 0; i < n; i++){
            out.append(vals[i]).append(' ');
        }
    }
}
