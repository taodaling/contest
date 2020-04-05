package on2020_04.on2020_04_04_Qualification_Round_2020___Code_Jam_2020.Indicium1;




import template.graph.HungrayAlgoMatrixStyle;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class Indicium {
    //Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int k = in.readInt() - n;
        int[][] mat = new int[n][n];
        SequenceUtils.deepFill(mat, -1);
        for (int i = 0; i < n; i++) {
            mat[i][i] = 0;
        }

        if (n <= 3) {
            int[][] ans = solve(n, k + n);
            if(ans == null){
                out.println("IMPOSSIBLE");
                return;
            }
            out.println("POSSIBLE");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.append(ans[i][j]).append(' ');
                }
                out.println();
            }
            return;
        }

        int sum = 0;
        int trace = n;
        while (sum < k) {
            trace--;
            int plus = Math.min(n - 1, k - sum);
            mat[trace][trace] += plus;
            sum += plus;
        }
        //debug.debug("mat", Arrays.deepToString(mat));
        if (trace == n - 1 && mat[trace][trace] == 1 || trace == 0 && mat[trace][trace] == n - 2) {
            out.println("IMPOSSIBLE");
            return;
        }

        if (k == 0 || k == (n - 1) * n) {
        } else if (trace == n - 1) {
            mat[trace][trace]--;
            mat[trace - 1][trace - 1]++;
        } else if (trace == 0) {
            mat[trace][trace]++;
            mat[trace + 1][trace + 1]--;
        } else if (trace == 1 && mat[trace][trace] == n - 1) {
            mat[trace - 1][trace - 1]++;
            mat[trace][trace]--;
        }
        //debug.debug("mat", Arrays.deepToString(mat));
        //debug.debug("trace", trace);
        IntegerList list = new IntegerList();
        IntegerList index = new IntegerList();
        for (int i = 0; i < n; i++) {
            if (list.isEmpty() || list.tail() != mat[i][i]) {
                list.push(mat[i][i]);
                index.push(i);
            }
        }

        if (list.size() == 3) {
            int a = list.get(0);
            int b = list.get(1);
            int c = list.get(2);
            trace = index.get(1);
            //1 x n
            //n 1     x
            //  n x 1
            //x     n 1
            //    1 x n

            for (int j = trace + 1; j < n; j++) {
                mat[j - 1][j] = a;
            }
            mat[n - 1][trace] = a;
            for (int j = trace - 1; j >= 0; j--) {
                mat[j + 1][j] = c;
            }
            mat[0][trace] = c;
            for (int j = 1; j < trace; j++) {
                mat[j - 1][j] = b;
            }
            for (int j = n - 2; j > trace; j--) {
                mat[j + 1][j] = b;
            }
            mat[trace - 1][n - 1] = b;
            mat[trace + 1][0] = b;
        } else if (list.size() == 2) {
            int a = list.get(0);
            int b = list.get(1);
            trace = index.get(1);
            //1 n
            //  1 n
            //n   1
            //      n   1
            //      1 n
            //        1 n
            for (int j = trace + 1; j < n; j++) {
                mat[j][j - 1] = a;
            }
            mat[trace][n - 1] = a;
            for (int j = 1; j < trace; j++) {
                mat[j - 1][j] = b;
            }
            mat[trace - 1][0] = b;
        }

        //debug.debug("mat", Arrays.deepToString(mat));

        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != -1) {
                    used[mat[i][j]] = true;
                }
            }
        }

        HungrayAlgoMatrixStyle ha = new HungrayAlgoMatrixStyle(n, n);
        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }
            ha.reset();
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < n; t++) {
                    if (mat[j][t] == -1) {
                        ha.addEdge(j, t, true);
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                ha.matchLeft(j);
            }
            for (int j = 0; j < n; j++) {
                mat[j][ha.leftPartner(j)] = i;
            }
        }

        out.println("POSSIBLE");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j] + 1).append(' ');
            }
            out.println();
        }
    }

    public boolean dfs(int[][] mat, boolean[][] row, boolean[][] col, int i, int j, int t) {
        if (j >= mat.length) {
            return dfs(mat, row, col, i + 1, 0, t);
        }
        if (i == mat.length) {
            int trace = 0;
            for (int k = 0; k < mat.length; k++) {
                trace += mat[k][k];
            }
            return trace == t;
        }
        for (int k = 1; k <= mat.length; k++) {
            if (row[i][k] || col[j][k]) {
                continue;
            }
            row[i][k] = col[j][k] = true;
            mat[i][j] = k;
            if (dfs(mat, row, col, i, j + 1, t)) {
                return true;
            }
            row[i][k] = col[j][k] = false;
        }
        return false;
    }

    private int[][] solve(int n, int k) {
        int[][] mat = new int[n][n];
        boolean[][] row = new boolean[n][n + 1];
        boolean[][] col = new boolean[n][n + 1];
        if (dfs(mat, row, col, 0, 0, k)) {
            return mat;
        }
        return null;
    }
}
