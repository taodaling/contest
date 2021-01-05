package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.SequenceUtils;

public class CThreeStates {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        mat = new char[n][m];
        boolean[][] obstacle = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc();
                obstacle[i][j] = mat[i][j] == '#';
            }
        }
        int[][][] bfsRes = new int[3][][];
        for (int i = 0; i < 3; i++) {
            bfsRes[i] = bfs((char) ('1' + i));
        }
        int[][] toEach = new int[3][3];
        SequenceUtils.deepFill(toEach, inf);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if (mat[j][k] >= '1' && mat[j][k] <= '3') {
                        int s = mat[j][k] - '1';
                        toEach[i][s] = Math.min(toEach[i][s], bfsRes[i][j][k]);
                    }
                }
            }
        }

        int ans = inf;
        for (int i = 0; i < 3; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    continue;
                }
                sum += toEach[i][j] - 1;
            }
            ans = Math.min(ans, sum);
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(mat[i][j] != '.'){
                    continue;
                }
                int sum = 1;
                for(int k = 0; k < 3; k++){
                    sum += bfsRes[k][i][j] - 1;
                }
                ans = Math.min(ans, sum);
            }
        }

        if(ans > 1e7){
            out.println(-1);
            return;
        }
        out.println(ans);
    }

    int inf = (int) 1e8;
    int[][] dirs = new int[][]{
            {-1, 0},
            {1, 0},
            {0, 1},
            {0, -1}
    };
    char[][] mat;

    public boolean valid(int i, int j) {
        return !(i < 0 || j < 0 || i >= mat.length || j >= mat[0].length || mat[i][j] == '#');
    }

    public int[][] bfs(char source) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] dists = new int[mat.length][mat[0].length];
        SequenceUtils.deepFill(dists, inf);
        LongDeque dq = new LongDequeImpl(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == source) {
                    dq.addFirst(DigitUtils.asLong(i, j));
                    dists[i][j] = 0;
                }
            }
        }
        while (!dq.isEmpty()) {
            long head = dq.removeFirst();
            int hx = DigitUtils.highBit(head);
            int hy = DigitUtils.lowBit(head);
            for (int[] d : dirs) {
                int x = hx + d[0];
                int y = hy + d[1];
                if (!valid(x, y)) {
                    continue;
                }
                if (dists[x][y] <= dists[hx][hy] + 1) {
                    continue;
                }
                dists[x][y] = dists[hx][hy] + 1;
                dq.addLast(DigitUtils.asLong(x, y));
            }
        }
        return dists;
    }
}
