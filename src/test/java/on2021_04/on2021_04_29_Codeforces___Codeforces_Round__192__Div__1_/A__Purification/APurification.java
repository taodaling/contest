package on2021_04.on2021_04_29_Codeforces___Codeforces_Round__192__Div__1_.A__Purification;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class APurification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] mat = new char[n][n];
        int[] row = new int[n];
        int[] col = new int[n];
        Arrays.fill(row, -1);
        Arrays.fill(col, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.rc();
                if (mat[i][j] == '.') {
                    row[i] = j;
                    col[j] = i;
                }
            }
        }

        int mr = Arrays.stream(row).min().orElse(-1);
        int mc = Arrays.stream(col).min().orElse(-1);
        if (mr == -1 && mc == -1) {
            out.println(-1);
            return;
        }
        if (mr >= 0) {
            for (int i = 0; i < n; i++) {
                out.append(i + 1).append(' ').append(row[i] + 1).println();
            }
        } else {
            for (int i = 0; i < n; i++) {
                out.append(col[i] + 1).append(' ').append(i + 1).println();
            }
        }
    }
}
