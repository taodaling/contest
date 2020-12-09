package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DCityModel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n + 2][m + 2];

        int area = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                mat[i][j] = in.rc() - '0';
                if (mat[i][j] > 0) {
                    area += 2;
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (mat[i][j] > mat[i][j - 1]) {
                    area += mat[i][j] - mat[i][j - 1];
                }
            }
            for (int j = m; j >= 1; j--) {
                if (mat[i][j] > mat[i][j + 1]) {
                    area += mat[i][j] - mat[i][j + 1];
                }
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (mat[j][i] > mat[j - 1][i]) {
                    area += mat[j][i] - mat[j - 1][i];
                }
            }
            for (int j = n; j >= 1; j--) {
                if (mat[j][i] > mat[j + 1][i]) {
                    area += mat[j][i] - mat[j + 1][i];
                }
            }
        }
        out.println(area);

    }
}
