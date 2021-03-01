package contest;

import template.algo.CommutativeUndoOperation;
import template.algo.OfflineUndoSegment;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DConveyorBelts {

    public int next(int i, int j, char c) {
        switch (c) {
            case '<':
                j--;
                break;
            case '>':
                j++;
                break;
            case '^':
                i--;
                break;
        }
        return id(i, j);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        int q = in.ri();
        char[][] mat = new char[n + 1][m + 1];
        int[][] time = new int[n + 1][m + 1];
        UndoDSU dsu = new UndoDSU((n + 1) * (m + 2));
        dsu.init();
        OfflineUndoSegment st = new OfflineUndoSegment(0, q - 1);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m + 1; j++) {
                if (i == 0 || j == 0 || j == m + 1) {
                    dsu.border[id(i, j)] = id(i, j);
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                mat[i][j] = in.rc();
                time[i][j] = -1;
            }
        }
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            char t = in.rc();
            if (t == 'A') {
                qs[i] = new Query(id(in.ri(), in.ri()));
            } else {
                int x = in.ri();
                int y = in.ri();
                st.update(time[x][y], i - 1, 0, q - 1, dsu.merge(id(x, y), next(x, y, mat[x][y])));
                time[x][y] = i;
                mat[x][y] = in.rc();
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int x = i;
                int y = j;
                if (time[i][j] >= 0) {
                    st.update(time[x][y], q - 1, 0, q - 1, dsu.merge(id(x, y), next(x, y, mat[x][y])));
                } else {
                    dsu.rawMerge(id(x, y), next(x, y, mat[x][y]));
                }
            }
        }

        st.solve(0, q - 1, i -> {
            if (qs[i] != null) {
                qs[i].ans = dsu.border[dsu.find(qs[i].xy)];
            }
        });

        for (Query query : qs) {
            if (query == null) {
                continue;
            }
            if (query.ans == -1) {
                out.println("-1 -1");
            } else {
                int x = query.ans / (m + 2);
                int y = query.ans % (m + 2);
                out.append(x).append(' ').append(y).println();
            }
        }
    }

    int n;
    int m;

    public int id(int i, int j) {
        return i * (m + 2) + j;
    }
}

class Query {
    int xy;
    int ans;

    public Query(int xy) {
        this.xy = xy;
    }
}

class Border {
    int i;
    int j;
}

class UndoDSU {
    int[] rank;
    int[] p;
    int[] border;

    public UndoDSU(int n) {
        rank = new int[n];
        p = new int[n];
        border = new int[n];
    }

    public void init() {
        Arrays.fill(rank, 1);
        Arrays.fill(p, -1);
        Arrays.fill(border, -1);
    }

    public int find(int x) {
        while (p[x] != -1) {
            x = p[x];
        }
        return x;
    }

    public int size(int x) {
        return rank[find(x)];
    }

    public void rawMerge(int a, int b) {
        int x = find(a);
        int y = find(b);
        if (x == y) {
            return;
        }
        if (rank[x] < rank[y]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        p[y] = x;
        rank[x] += rank[y];
        if (border[x] == -1) {
            border[x] = border[y];
        }
    }

    public CommutativeUndoOperation merge(int a, int b) {
        return new CommutativeUndoOperation() {
            int x, y;
            int oldBorder;

            public void apply() {
                x = find(a);
                y = find(b);
                if (x == y) {
                    return;
                }
                if (rank[x] < rank[y]) {
                    int tmp = x;
                    x = y;
                    y = tmp;
                }
                p[y] = x;
                rank[x] += rank[y];
                oldBorder = border[x];
                if (border[x] == -1) {
                    border[x] = border[y];
                }
            }


            public void undo() {
                if (x == y) {
                    return;
                }
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                }
                p[y] = -1;
                border[x] = oldBorder;
            }
        };
    }
}
