package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.FillGridWithL;
import template.utils.SequenceUtils;

public class FillingTrominos {
    int[][] mat;
    int[][] color;
    int[][] dirs = new int[][]{
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] ans = new FillGridWithL().solve(n, m);
        if(ans == null){
            out.println("NO");
            return;
        }
        int[][] color = color(ans);
        output(color, out);
    }

    public void output(int[][] mat, FastOutput out) {
        out.println("YES");
        for (int i = 0; i < mat.length; i++) {
            for (int x : mat[i]) {
                out.append((char) ('A' + x));
            }
            out.println();
        }
    }

    public void setColor(int i, int j, int v, int fi, int fj, int c) {
        if (v != mat[i][j]) {
            return;
        }
        color[i][j] = c;
        for (int[] d : dirs) {
            int x = d[0] + i;
            int y = d[1] + j;
            if (!valid(x, y)) {
                continue;
            }
            if (x == fi && y == fj) {
                continue;
            }
            setColor(x, y, v, i, j, c);
        }
    }

    public boolean valid(int i, int j) {
        return i >= 0 && j >= 0 && i < mat.length && j < mat[0].length;
    }

    public int findColor(int i, int j, int v, int fi, int fj) {
        if (v != mat[i][j]) {
            if (color[i][j] == -1) {
                return 0;
            }
            return 1 << color[i][j];
        }
        int ans = 0;
        for (int[] d : dirs) {
            int x = d[0] + i;
            int y = d[1] + j;
            if (!valid(x, y)) {
                continue;
            }
            if (x == fi && y == fj) {
                continue;
            }
            ans |= findColor(x, y, v, i, j);
        }
        return ans;
    }

    public int[][] color(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        this.mat = mat;
        color = new int[n][m];
        SequenceUtils.deepFill(color, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (color[i][j] == -1) {
                    int bits = findColor(i, j, mat[i][j], -1, -1);
                    int c = 0;
                    while (Bits.get(bits, c) == 1) {
                        c++;
                    }
                    setColor(i, j, mat[i][j], -1, -1, c);
                }
            }
        }
        return color;
    }
}
