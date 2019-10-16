package contest;

import java.util.Arrays;

import template.FastInput;
import template.FastOutput;

public class TaskD {


    int[][] f;
    int[][] g;
    int[][] grid;
    int[][][] prefix;
    int c1;
    int c2;
    int inf = (int) 1e8;
    int h;
    int w;
    int range = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        h = in.readInt();
        w = in.readInt();
        grid = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                grid[i][j] = in.readChar() - 'a';
            }
        }

        prefix = new int[h][w][range];
        for (int j = 0; j < w; j++) {
            prefix[0][j][grid[0][j]]++;
            for (int i = 1; i < h; i++) {
                System.arraycopy(prefix[i - 1][j], 0, prefix[i][j], 0, range);
                prefix[i][j][grid[i][j]]++;
            }
        }


        f = new int[h][h];
        g = new int[h][h];
        long ans = 0;
        for (int i = 1; i < w; i++) {
            c1 = i - 1;
            c2 = i;

            for (int[] f0 : f) {
                Arrays.fill(f0, -1);
            }
            for (int[] g0 : g) {
                Arrays.fill(g0, -1);
            }

            ans += f(h - 1, h - 1);
        }

        out.println(ans);
    }

    public int g(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }

        if (g[i][j] == -1) {
            g[i][j] = g(i - 1, j - 1);
            if (grid[i][c1] == grid[j][c2]) {
                g[i][j]++;
            }
        }
        return g[i][j];
    }

    public int f(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }

        if (f[i][j] == -1) {
            f[i][j] = Math.min(f(i - 1, j), f(i, j - 1)) + g(i, j);
        }
        return f[i][j];
    }
}
