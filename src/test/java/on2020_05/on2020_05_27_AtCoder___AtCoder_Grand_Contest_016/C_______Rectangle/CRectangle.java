package on2020_05.on2020_05_27_AtCoder___AtCoder_Grand_Contest_016.C_______Rectangle;



import template.io.FastInput;
import template.io.FastOutput;

public class CRectangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int H = in.readInt();
        int W = in.readInt();
        int h = in.readInt();
        int w = in.readInt();
        if (H % h == 0 && W % w == 0) {
            out.println("No");
            return;
        }
        out.println("Yes");
        int[][] ans = null;
        if (H % h != 0) {
            ans = solve(W, H, w, h);
            ans = transpose(ans);
        } else {
            ans = solve(H, W, h, w);
        }

        for (int[] row : ans) {
            for (int x : row) {
                out.append(x).append(' ');
            }
            out.println();
        }
    }

    public int[][] transpose(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] ans = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[j][i] = mat[i][j];
            }
        }
        return ans;
    }

    //W % w != 0
    public int[][] solve(int H, int W, int h, int w) {
        int r = W % w;
        int[] row = new int[W];
        for (int i = 0; i < W; i++) {
            if (i % w == 0) {
                row[i] = (int) 1e9 - 1;
            } else if (i % w == r) {
                row[i] = (int) -1e9;
            }
        }
        int[][] ans = new int[H][];
        for (int i = 0; i < H; i++) {
            ans[i] = row.clone();
        }
        return ans;
    }
}
