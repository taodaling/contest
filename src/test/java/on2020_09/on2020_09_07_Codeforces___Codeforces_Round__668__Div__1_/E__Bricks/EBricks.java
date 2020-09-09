package on2020_09.on2020_09_07_Codeforces___Codeforces_Round__668__Div__1_.E__Bricks;



import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class EBricks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }

        int down = 0;
        int right = 1;
        int[][][] id = new int[2][n][m];
        int lrId = 0;
        int udId = 0;
        SequenceUtils.deepFill(id, -1);
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '.') {
                    continue;
                }
                cnt++;
                if (i + 1 < n && mat[i + 1][j] == '#') {
                    id[down][i][j] = udId++;
                }
                if (j + 1 < m && mat[i][j + 1] == '#') {
                    id[right][i][j] = lrId++;
                }
            }
        }

        DinicBipartiteMatch bm = new DinicBipartiteMatch(lrId, udId);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (id[right][i][j] == -1) {
                    continue;
                }
                for (int dx = 0; dx >= -1; dx--) {
                    for (int dy = 0; dy <= 1; dy++) {
                        int x = dx + i;
                        int y = dy + j;
                        if (x < 0 || x >= n || y < 0 || y >= m) {
                            continue;
                        }
                        if (id[down][x][y] == -1) {
                            continue;
                        }
                        bm.addEdge(id[right][i][j], id[down][x][y]);
                    }
                }
            }
        }

        int maxMatch = bm.solve();
        int maxIndependentSet = lrId + udId - maxMatch;
        int ans = cnt - maxIndependentSet;
        out.println(ans);
    }
}
