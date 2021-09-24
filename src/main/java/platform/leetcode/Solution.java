package platform.leetcode;


import java.util.*;

class Solution {
    public static void main(String[] args) {
        int ans = new Solution().cutOffTree(Arrays.asList(
                Arrays.asList(4, 2, 3),
                Arrays.asList(0, 0, 1),
                Arrays.asList(7, 6, 5)
        ));
        System.out.println(ans);
    }

    public int cutOffTree(List<List<Integer>> forest) {
        int n = forest.size();
        int m = forest.get(0).size();
        int[][] mat = new int[n][m];
        boolean[][] passable = new boolean[n][m];
        int[][] pos = new int[n * m][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = forest.get(i).get(j);
                passable[i][j] = mat[i][j] > 0;
                pos[i * m + j] = new int[]{i, j};
            }
        }
        Arrays.sort(pos, Comparator.comparingInt(x -> mat[x[0]][x[1]]));
        int[] last = new int[2];
        int wpos = 0;
        int[][] qs = new int[4][n * m + 1];
        for (int i = 0; i < n * m; i++) {
            int[] xy = pos[i];
            if (mat[xy[0]][xy[1]] <= 1) {
                continue;
            }
            qs[0][wpos] = last[0];
            qs[1][wpos] = last[1];
            qs[2][wpos] = xy[0];
            qs[3][wpos] = xy[1];
            last = xy;
            wpos++;
        }
        UnweightedGridBatchShortestPath gbsp = new UnweightedGridBatchShortestPath(passable, DIR4);

        int[] ans = gbsp.query(qs[0], qs[1], qs[2], qs[3]);
        for (int x : ans) {
            if (x == UnweightedGridBatchShortestPath.inf) {
                return -1;
            }
        }
        int sum = Arrays.stream(ans).sum();
        return sum;
    }

    public static final int[][] DIR4 = new int[][]{
            {1, 0},
            {0, -1},
            {-1, 0},
            {0, 1}
    };
}

class UnweightedGridBatchShortestPath {
    boolean[][] passable;
    int[][] dirs;
    int n;
    int m;
    Deque<int[]> dq;
    public static int inf = (int) 1e9;

    /**
     * -1 <= dirs[i][0,1] <= 1
     *
     * @param passable
     * @param dirs
     */
    public UnweightedGridBatchShortestPath(boolean[][] passable, int[][] dirs) {
        this.passable = passable;
        this.dirs = dirs;
        n = passable.length;
        m = passable[0].length;
        dq = new ArrayDeque<>(n * m);
        dist = new int[n][m];
    }

    /**
     * <p>
     * the answer for the i-th query is the shortest path between (srcX[i], srcY[i]) to (dstX[i], dstY[i])
     * </p>
     * <p>
     * time complexity: O(nm(n+m) + q\log_2nm)
     * </p>
     * <p>
     * res[i] = inf means there isn't a path between the two positions
     * </p>
     */
    public int[] query(int[] srcX, int[] srcY, int[] dstX, int[] dstY) {
        int k = srcX.length;
        qs = new Query[k];
        for (int i = 0; i < k; i++) {
            qs[i] = new Query(srcX[i], srcY[i], dstX[i], dstY[i], inf);
        }
        Query[] original = qs.clone();
        buf = new Query[k];
        dac(0, n - 1, 0, m - 1, 0, k - 1);
        int[] ans = new int[k];
        for (int i = 0; i < k; i++) {
            ans[i] = original[i].ans;
        }
        return ans;
    }


    Query[] buf;
    Query[] qs;
    int[][] dist;

    private void process(int b, int t, int l, int r, int srcX, int srcY, int L, int R) {
        if (!passable[srcX][srcY]) {
            return;
        }

        for (int i = b; i <= t; i++) {
            for (int j = l; j <= r; j++) {
                dist[i][j] = inf;
            }
        }
        dist[srcX][srcY] = 0;
        assert dq.isEmpty();
        dq.addLast(new int[]{srcX, srcY});
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            int x = head[0];
            int y = head[1];
            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (b <= nx && nx <= t && l <= ny && ny <= r && passable[nx][ny] && dist[nx][ny] > dist[x][y] + 1) {
                    dist[nx][ny] = dist[x][y] + 1;
                    dq.addLast(new int[]{nx, ny});
                }
            }
        }
        for (int i = L; i <= R; i++) {
            qs[i].ans = Math.min(qs[i].ans, dist[qs[i].ax][qs[i].ay] +
                    dist[qs[i].bx][qs[i].by]);
        }
    }

    private void dac(int b, int t, int l, int r, int L, int R) {
        if (L > R) {
            return;
        }
        if (b == t && l == r) {
            if (passable[b][l]) {
                for (int i = L; i <= R; i++) {
                    qs[i].ans = 0;
                }
            }
            return;
        }
        if (t - b >= r - l) {
            int m = (t + b) / 2;
            for (int i = l; i <= r; i++) {
                process(b, t, l, r, m, i, L, R);
            }
            int bufWpos = 0;
            int qsWpos = L;
            for (int i = L; i <= R; i++) {
                Query q = qs[i];
                if (q.ax < m && q.bx < m) {
                    qs[qsWpos++] = q;
                } else if (q.ax > m && q.bx > m) {
                    buf[bufWpos++] = q;
                }
            }
            System.arraycopy(buf, 0, qs, qsWpos, bufWpos);
            dac(b, m - 1, l, r, L, qsWpos - 1);
            dac(m + 1, t, l, r, qsWpos, qsWpos + bufWpos - 1);
        } else {
            int m = (l + r) / 2;
            for (int i = b; i <= t; i++) {
                process(b, t, l, r, i, m, L, R);
            }
            int bufWpos = 0;
            int qsWpos = L;
            for (int i = L; i <= R; i++) {
                Query q = qs[i];
                if (q.ay < m && q.by < m) {
                    qs[qsWpos++] = q;
                } else if (q.ay > m && q.by > m) {
                    buf[bufWpos++] = q;
                }
            }
            System.arraycopy(buf, 0, qs, qsWpos, bufWpos);
            dac(b, t, l, m - 1, L, qsWpos - 1);
            dac(b, t, m + 1, r, qsWpos, qsWpos + bufWpos - 1);
        }
    }


    static class Query {
        int ax;
        int ay;
        int bx;
        int by;
        int ans;

        public Query(int ax, int ay, int bx, int by, int ans) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.ans = ans;
        }
    }
}
