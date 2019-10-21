package on2019_10.on2019_10_20_AGC006.TaskE;



import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[3][n];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        //check1
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < 3; j++) {
                if ((mat[j][i] - 1) / 3 != (mat[j - 1][i] - 1) / 3) {
                    no(out);
                    return;
                }
            }
        }

        //check2
        for (int i = 0; i < n; i++) {
            int whichCol = (mat[0][i] - 1) / 3;
            int dist = Math.abs(i - whichCol);
            if (dist % 2 != 0) {
                no(out);
                return;
            }
        }

        //check3
        int inv = 0;
        for (int i = 0; i < n; i++) {
            if (mat[0][i] < mat[1][i] && mat[1][i] < mat[2][i]) {
                continue;
            }
            if (mat[0][i] > mat[1][i] && mat[1][i] > mat[2][i]) {
                inv++;
                continue;
            }
            no(out);
        }

        if (inv % 3 != 0) {
            no(out);
        } else {
            yes(out);
        }
    }

    public void yes(FastOutput out) {
        out.println("Yes");
    }

    public void no(FastOutput out) {
        out.println("No");
    }

}
