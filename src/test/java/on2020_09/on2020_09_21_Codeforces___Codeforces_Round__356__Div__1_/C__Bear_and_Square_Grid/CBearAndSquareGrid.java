package on2020_09.on2020_09_21_Codeforces___Codeforces_Round__356__Div__1_.C__Bear_and_Square_Grid;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CBearAndSquareGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        mat = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = in.readChar() == '.' ? 0 : 1;
            }
        }
        ps = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                ps[i][j] = mat[i][j];
                ps[i][j] += ps[i][j - 1];
            }
            for (int j = 1; j <= n; j++) {
                ps[i][j] += ps[i - 1][j];
            }
        }

        cc = new int[n + 2][n + 2];
        size = new int[(n + 2) * (n + 2) + 1];
        SequenceUtils.deepFill(cc, 0);
        int order = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (mat[i][j] == 0 && cc[i][j] == 0) {
                    size[order] = dfs(i, j, order);
                    order++;
                }
            }
        }
        conn = new int[order];
        visited = new boolean[n + 2][n + 2];

        for (int i = 0; i <= k + 1; i++) {
            for (int j = 0; j <= k + 1; j++) {
                add(i, j, 1);
            }
        }
        l = 0;
        r = k + 1;
        b = 0;
        t = k + 1;

        dfsForMove();

        out.println(ans);
    }

    boolean[][] visited;


    public void dfsForMove() {
        visited[b][l] = true;
        update();
        if (t + 1 <= n + 1 && !visited[b + 1][l]) {
            moveDown();
        } else if (r + 1 <= n + 1 && !visited[b][l + 1]) {
            moveRight();
        } else if (b - 1 >= 0 && !visited[b - 1][l]) {
            moveUp();
        } else if (l - 1 >= 0 && !visited[b][l - 1]) {
            moveLeft();
        } else {
            return;
        }
        dfsForMove();
    }

    public void update() {
        add(b, l, -1);
        add(t, l, -1);
        add(b, r, -1);
        add(t, r, -1);

        int cand = sum + region(b + 1, t - 1, l + 1, r - 1);
        ans = Math.max(ans, cand);


        add(b, l, 1);
        add(t, l, 1);
        add(b, r, 1);
        add(t, r, 1);
    }

    int k;
    int ans;
    int sum;
    int[] conn;
    int b;
    int t;
    int l;
    int r;

    public void moveDown() {
        for (int i = l; i <= r; i++) {
            add(b, i, -1);
            add(t + 1, i, 1);
        }
        b++;
        t++;
    }

    public void moveUp() {
        for (int i = l; i <= r; i++) {
            add(b - 1, i, 1);
            add(t, i, -1);
        }
        b--;
        t--;
    }

    public void moveRight() {
        for (int i = b; i <= t; i++) {
            add(i, l, -1);
            add(i, r + 1, 1);
        }
        l++;
        r++;
    }

    public void moveLeft() {
        for (int i = b; i <= t; i++) {
            add(i, l - 1, 1);
            add(i, r, -1);
        }
        l--;
        r--;
    }


    public boolean check(int i, int j) {
        return !(i < 1 || j < 1 || i > n || j > n);
    }

    public void add(int i, int j, int x) {
        int id = cc[i][j];
        if (conn[id] == 0) {
            sum += size[id];
        }
        conn[id] += x;
        if (conn[id] == 0) {
            sum -= size[id];
        }
    }

    int n;
    int[][] mat;
    int[][] cc;
    int[] size;
    int[][] ps;


    public int region(int b, int t, int l, int r) {
        int ans = ps[t][r];
        if (l > 0) {
            ans -= ps[t][l - 1];
        }
        if (b > 0) {
            ans -= ps[b - 1][r];
        }
        if (l > 0 && b > 0) {
            ans += ps[b - 1][l - 1];
        }
        return ans;
    }

    public int dfs(int i, int j, int id) {
        if (!check(i, j) || mat[i][j] == 1 || cc[i][j] != 0) {
            return 0;
        }
        cc[i][j] = id;
        return dfs(i + 1, j, id) + dfs(i - 1, j, id) + dfs(i, j - 1, id) + dfs(i, j + 1, id) + 1;
    }
}
