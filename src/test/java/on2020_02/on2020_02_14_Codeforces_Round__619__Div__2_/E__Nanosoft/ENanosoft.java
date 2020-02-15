package on2020_02.on2020_02_14_Codeforces_Round__619__Div__2_.E__Nanosoft;



import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.MultiWayIntegerStack;
import template.utils.SequenceUtils;

public class ENanosoft {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        char[][] mat = new char[n + 1][m + 1];
        int[][] rep = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                mat[i][j] = in.readChar();
            }
        }
        for (int i = n; i >= 1; i--) {
            for (int j = m; j >= 1; j--) {
                if (i + 1 <= n && j + 1 <= m && mat[i][j] == mat[i + 1][j] &&
                        mat[i][j] == mat[i][j + 1] && mat[i][j] == mat[i + 1][j + 1]) {
                    rep[i][j] = 1 + rep[i + 1][j + 1];
                } else {
                    rep[i][j] = 1;
                }
            }
        }

        MultiWayStack<int[]> stack = new MultiWayStack<>(n + 1, n * m);
        int[][] size = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int r = rep[i][j];
                int x = i + r;
                int y = j + r;
                if (x > n || y > m) {
                    continue;
                }
                if (mat[i][j] == 'R' && mat[i][y] == 'G' &&
                        mat[x][j] == 'Y' && mat[x][y] == 'B' &&
                        rep[i][y] >= r && rep[x][j] >= r && rep[x][y] >= r) {
                    size[i][j] = r * 2;
                    stack.addLast(r * 2, SequenceUtils.wrapArray(i, j));
                }
            }
        }

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].x1 = in.readInt();
            qs[i].y1 = in.readInt();
            qs[i].x2 = in.readInt();
            qs[i].y2 = in.readInt();
        }

        int[][] ps = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            SequenceUtils.deepFill(ps, 0);
            while (!stack.isEmpty(i)) {
                int[] xy = stack.removeLast(i);
                ps[xy[0]][xy[1]] = 1;
            }
            presum(ps);
            for (Query query : qs) {
                int x2 = query.x2 - i + 1;
                int y2 = query.y2 - i + 1;
                int cnt = rect(ps, query.x1, query.y1, x2, y2);
                if (cnt > 0) {
                    query.ans = i;
                }
            }
        }

        for (Query query : qs) {
            out.println(query.ans * query.ans);
        }
    }

    public void presum(int[][] ps) {
        int n = ps.length;
        int m = ps[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < m; j++) {
                ps[i][j] += ps[i][j - 1];
            }
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ps[i][j] += ps[i - 1][j];
            }
        }
    }

    public int rect(int[][] ps, int x1, int y1, int x2, int y2) {
        if (x1 > x2 || y1 > y2) {
            return 0;
        }
        return ps[x2][y2] - ps[x1 - 1][y2] - ps[x2][y1 - 1] + ps[x1 - 1][y1 - 1];
    }
}

class Query {
    int x1;
    int y1;
    int x2;
    int y2;
    int ans;
}