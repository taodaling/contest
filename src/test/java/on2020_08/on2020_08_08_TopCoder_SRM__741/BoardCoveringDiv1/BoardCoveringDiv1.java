package on2020_08.on2020_08_08_TopCoder_SRM__741.BoardCoveringDiv1;



import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class BoardCoveringDiv1 {
    //Debug debug = new Debug(true);
    public String[] make(int N, int R, int C) {
        if ((N * N - 1) % 3 != 0) {
            return new String[0];
        }
        if (N == 1) {
            return new String[]{"#"};
        }
        int[][] mat = make(N, N, R, C);
        if (!valid) {
            return new String[0];
        }
        int[][] ans = map(mat, R, C);
        //debug.debug("mat", Arrays.deepToString(mat));
        String[] s = new String[N];
        for (int i = 0; i < N; i++) {
            StringBuilder builder = new StringBuilder();
            for (int x : ans[i]) {
                builder.append((char) x);
            }
            s[i] = builder.toString();
        }
        return s;
    }


    public int[][] map(int[][] mat, int x, int y) {
        int n = mat.length;
        int m = mat[0].length;

        int[][] ans = new int[n][m];
        SequenceUtils.deepFill(ans, 9);
        boolean[] occur = new boolean[10];
        boolean[][] visited = new boolean[n][m];
        Deque<int[]> indices = new ArrayDeque<>();
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == x && j == y) {
                    continue;
                }
                if (visited[i][j]) {
                    continue;
                }
                Arrays.fill(occur, false);
                visited[i][j] = true;
                indices.add(new int[]{i, j});
                list.clear();
                while (!indices.isEmpty()) {
                    int[] head = indices.removeFirst();
                    list.add(head);
                    for (int[] dir : dirs) {
                        int nx = head[0] + dir[0];
                        int ny = head[1] + dir[1];
                        if (nx < 0 || ny < 0 || nx >= n || ny >= m || nx == x && ny == y) {
                            continue;
                        }
                        if (mat[nx][ny] == mat[i][j] && !visited[nx][ny]) {
                            visited[nx][ny] = true;
                            indices.addLast(new int[]{nx, ny});
                        } else if (mat[nx][ny] != mat[i][j]) {
                            occur[ans[nx][ny]] = true;
                        }
                    }
                }

                int alloc = mex(occur);
                for (int[] index : list) {
                    ans[index[0]][index[1]] = alloc;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] += '0';
            }
        }

        ans[x][y] = '#';

        return ans;
    }

    public int mex(boolean[] visited) {
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    public void copy(int[][] src, int[][] target, int b, int t, int l, int r) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                target[i + b][j + l] = src[i][j];
            }
        }
        return;
    }

    public int mirror(int n, int i) {
        return n - 1 - i;
    }

    public void upDownFlip(int[][] mat) {
        for (int i = 0; i < mat[0].length; i++) {
            int l = 0;
            int r = mat.length - 1;
            while (l < r) {
                int tmp = mat[l][i];
                mat[l][i] = mat[r][i];
                mat[r][i] = tmp;
                l++;
                r--;
            }
        }
    }

    public void leftDownFlip(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            int l = 0;
            int r = mat[0].length - 1;
            while (l < r) {
                int tmp = mat[i][l];
                mat[i][l] = mat[i][r];
                mat[i][r] = tmp;
                l++;
                r--;
            }
        }
    }

    public int[][] rotate(int[][] mat) {
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

    int order = 0;

    public int nextOrder() {
        return ++order;
    }

    boolean valid = true;

    public boolean visit(int[][] mat, int i, int j) {
        if (i < 0 || j < 0 || i >= mat.length || j >= mat[0].length || mat[i][j] > 0) {
            return false;
        }
        return true;
    }

    int[][] dirs = new int[][]{
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };

    public void move(int[][] mat, int i, int j, int fail, int used, int cur) {
        if (fail >= 4) {
            return;
        }
        if (used == 3) {
            nextOrder();
            used = 0;
        }
        mat[i][j] = order;
        if (visit(mat, i + dirs[cur][0], j + dirs[cur][1])) {
            move(mat, i + dirs[cur][0], j + dirs[cur][1], 0, used + 1, cur);
            return;
        }
        move(mat, i, j, fail + 1, used, (cur + 1) % 4);
    }

    public int[][] make(int h, int w, int x, int y) {
        if (x >= h - x) {
            int[][] ans = make(h, w, mirror(h, x), y);
            upDownFlip(ans);
            return ans;
        }
        if (y >= w - y) {
            int[][] ans = make(h, w, x, mirror(w, y));
            leftDownFlip(ans);
            return ans;
        }
        if (h < w || h == w && h - x < w - y) {
            int[][] ans = make(w, h, y, x);
            return rotate(ans);
        }

        int[][] ans = new int[h][w];
        if ((h > 4) && h - x - 1 >= 3) {
            for (int i = 0; i < w; i++) {
                int c = nextOrder();
                for (int j = 0; j < 3; j++) {
                    ans[h - j - 1][i] = c;
                }
            }
            int[][] sub = make(h - 3, w, x, y);
            copy(sub, ans, 0, h - 4, 0, w - 1);
            return ans;
        }
        int[][] mat = new int[h][w];
        mat[x][y] = nextOrder();
        nextOrder();
        if (h == 4 && w == 1) {
            if (x > 0) {
                valid = false;
                return mat;
            }
            move(mat, 1, 0, 0, 0, 2);
            return mat;
        }

        if (h == 4 && w == 4) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (mat[i][j] == 0) {
                        mat[i][j] = order;
                    }
                }
            }
            nextOrder();
            move(mat, 2, 0, 0, 0, 2);
            return mat;
        }

        if (h == 2 && w == 2) {
            move(mat, 1, 0, 0, 0, 2);
            return mat;
        }

        if (h == 5 && w == 2) {
            move(mat, 3, 0, 0, 0, 2);
            return mat;
        }
        if (h == 5 && w == 5) {
            move(mat, 0, 0, 0, 0, 2);
            return mat;
        }

        throw new RuntimeException();
    }
}
