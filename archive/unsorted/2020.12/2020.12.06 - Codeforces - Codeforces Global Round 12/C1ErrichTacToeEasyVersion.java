package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.Debug;

import java.util.Arrays;

public class C1ErrichTacToeEasyVersion {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] mat = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.rc();
            }
        }
        int[] cnts = new int[3];
        int[] cnts2 = new int[3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 'X') {
                    cnts[(i + j) % 3]++;
                }
                if (mat[i][j] == 'O') {
                    cnts2[(i + j) % 3]++;
                }
            }
        }
        debug.debugArray("cnts", cnts);
        int index = -1;
        int index2 = -1;
        int sum = Arrays.stream(cnts).sum() + Arrays.stream(cnts2).sum();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j && (cnts[i] + cnts2[j]) * 3 <= sum) {
                    index = i;
                    index2 = j;
                    break;
                }
            }
        }
        assert index >= 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 'X' && (i + j) % 3 == index) {
                    mat[i][j] = 'O';
                } else if (mat[i][j] == 'O' && (i + j) % 3 == index2) {
                    mat[i][j] = 'X';
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j]);
            }
            out.println();
        }
    }
}
