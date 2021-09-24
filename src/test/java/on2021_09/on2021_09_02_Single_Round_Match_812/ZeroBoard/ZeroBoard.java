package on2021_09.on2021_09_02_Single_Round_Match_812.ZeroBoard;



import java.util.ArrayList;
import java.util.List;

public class ZeroBoard {
    public int[] solve(int R, int C, int[] data) {
        mat = new int[R][C];
        int[] sum = new int[2];

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                mat[i][j] = data[i * C + j];
                sum[(i + j) & 1] += mat[i][j];
            }
        }
        if (sum[0] != sum[1]) {
            return new int[]{-1};
        }
        for (int j = C - 1; j >= 2; j--) {
            for (int i = 0; i < R; i++) {
                add(i, j - 2, 1, mat[i][j]);
                add(i, j - 1, 1, -mat[i][j]);
            }
        }
        for (int i = R - 1; i >= 2; i--) {
            for (int j = 0; j < C; j++) {
                add(i - 2, j, 0, mat[i][j]);
                add(i - 1, j, 0, -mat[i][j]);
            }
        }
        if (R > 1 && C > 1) {
            add(0, 0, 1, mat[1][1]);
            add(0, 1, 0, -mat[1][1]);
            add(0, 0, 0, mat[0][1]);
            add(0, 0, 1, -mat[0][1]);
        }

        if (R > 1) {
            add(0, 0, 0, -mat[0][0]);
        } else if (C > 1) {
            add(0, 0, 1, -mat[0][0]);
        }
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (mat[i][j] != 0) {
                    throw new RuntimeException();
                }
            }
        }
        return sol.stream().mapToInt(Integer::intValue).toArray();
    }

    List<Integer> sol = new ArrayList<>();
    int[][] mat;


    void add(int r, int c, int d, int a) {
        if (a == 0) {
            return;
        }

        sol.add(r);
        sol.add(c);
        sol.add(d);
        sol.add(a);

        mat[r][c] += a;
        if (d == 0) {
            mat[r + 1][c] += a;
        } else {
            mat[r][c + 1] += a;
        }
    }
}
