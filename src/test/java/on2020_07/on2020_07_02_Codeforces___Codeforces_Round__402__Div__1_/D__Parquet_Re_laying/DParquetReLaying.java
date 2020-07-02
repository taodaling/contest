package on2020_07.on2020_07_02_Codeforces___Codeforces_Round__402__Div__1_.D__Parquet_Re_laying;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class DParquetReLaying {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }
        char[][] target = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(target[i], 0);
        }

        List<int[]> cast1 = solve(mat);
        List<int[]> cast2 = solve(target);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] != target[i][j]) {
                    out.println(-1);
                    return;
                }
            }
        }

        SequenceUtils.reverse(cast2);
        cast1.addAll(cast2);
        out.println(cast1.size());
        for (int[] xy : cast1) {
            out.append(xy[0] + 1).append(' ').append(xy[1] + 1).println();
        }
    }

    List<int[]> steps = new ArrayList<>((int) 1e5);

    public void rotate(char[][] mat, int i, int j) {
        steps.add(new int[]{i, j});
        if (mat[i][j] == 'L') {
            mat[i][j + 1] = mat[i][j] = 'U';
            mat[i + 1][j + 1] = mat[i + 1][j] = 'D';
        } else {
            mat[i][j] = mat[i + 1][j] = 'L';
            mat[i][j + 1] = mat[i + 1][j + 1] = 'R';
        }
    }

    public String toString(char[][] mat) {
        StringBuilder builder = new StringBuilder("\n");
        for (char[] row : mat) {
            builder.append(String.valueOf(row)).append('\n');
        }
        return builder.toString();
    }

    Debug debug = new Debug(true);

    public List<int[]> solve(char[][] mat) {
        steps.clear();

        int n = mat.length;
        int m = mat[0].length;

        while (true) {
           // debug.debug("mat", toString(mat));
            boolean goon = false;
            for (int i = 0; i < n - 1 && !goon; i++) {
                for (int j = 0; j < m - 1 && !goon; j++) {
                    if (mat[i][j] == 'U' && mat[i][j + 1] == 'U') {
                        rotate(mat, i, j);
                        goon = true;
                    } else if (mat[i][j] == 'U' && mat[i][j + 1] == 'L' && mat[i + 1][j + 1] == 'L') {
                        rotate(mat, i, j + 1);
                        goon = true;
                    }
                }
            }
            if (!goon) {
                break;
            }
        }

        return new ArrayList<>(steps);
    }
}
