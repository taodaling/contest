package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CNuskeVsPhantomThnook {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        //even for vertex, odd for edge
        int[][] mat = new int[n + 1][m + 1];
        //edge with top
        int[][] colEdge = new int[n + 1][m + 1];
        //edge with right
        int[][] rowEdge = new int[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() - '0';
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] + mat[i + 1][j] == 2) {
                    colEdge[i][j] = 1;
                }
                if (mat[i][j] + mat[i][j + 1] == 2) {
                    rowEdge[i][j] = 1;
                }
            }
        }

        presum(mat);
        presum(colEdge);
        presum(rowEdge);

        for (int i = 0; i < q; i++) {
            int b = in.readInt() - 1;
            int l = in.readInt() - 1;
            int t = in.readInt() - 1;
            int r = in.readInt() - 1;

            int vertexCnt = region(mat, b, t, l, r);
            int edge = region(colEdge, b, t - 1, l, r) +
                    region(rowEdge, b, t, l, r - 1);

            int cc = vertexCnt - edge;
            out.println(cc);
        }
    }

    public int region(int[][] mat, int b, int t, int l, int r) {
        if (b > t || l > r) {
            return 0;
        }

        int ans = mat[t][r];
        if (b > 0) {
            ans -= mat[b - 1][r];
        }
        if (l > 0) {
            ans -= mat[t][l - 1];
        }
        if (b > 0 && l > 0) {
            ans += mat[b - 1][l - 1];
        }
        return ans;
    }

    public void presum(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < m; j++) {
                mat[i][j] += mat[i][j - 1];
            }
            if (i > 0) {
                for (int j = 0; j < m; j++) {
                    mat[i][j] += mat[i - 1][j];
                }
            }
        }
    }
}
