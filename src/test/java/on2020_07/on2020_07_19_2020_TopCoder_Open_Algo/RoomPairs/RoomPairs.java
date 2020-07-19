package on2020_07.on2020_07_19_2020_TopCoder_Open_Algo.RoomPairs;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomPairs {
    public String[] construct(int R, int C, int N) {

        int order = 0;

        boolean rotate = R > C;
        if (rotate) {
            int tmp = R;
            R = C;
            C = tmp;
        }

        int[][] mat = new int[R][C];
        for (int i = 0; i < R - 1 && N > 0; i++) {
            N--;
            Arrays.fill(mat[i], ++order);
        }


        int row = 0;
        int col = 0;
        while (N > 0) {
            if (col >= C - 1) {
                row++;
                col = 0;
            }
            int add = row == R - 1 ? 1 : 2;
            if (N >= add) {
                N -= add;
                mat[row][col] = ++order;
                col++;
                continue;
            }
            //add = 2 and n = 1
            if (row < R - 2) {
                mat[row][col] = ++order;
                Arrays.fill(mat[R - 1], mat[R - 2][0]);
            } else if (col > 0) {
                mat[row + 1][0] = ++order;
            } else {
                if (C <= 2) {
                    return new String[0];
                }
                Arrays.fill(mat[R - 1], mat[R - 2][0]);
                mat[R - 1][0] = ++order;
                mat[R - 1][C - 1] = ++order;
            }
            break;
        }

        if (rotate) {
            mat = rotate(mat);
        }

        String[] ans = build(mat);
        debug(ans);
        return ans;
    }

    public String[] build(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        List<String> ans = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            //build top
            StringBuilder top = new StringBuilder();
            top.append("+");
            for (int j = 0; j < m; j++) {
                if (i == n || i == 0 || mat[i - 1][j] != mat[i][j]) {
                    top.append('-');
                } else {
                    top.append(' ');
                }
                top.append('+');
            }
            ans.add(top.toString());
            if (i == n) {
                break;
            }
            //build lr
            StringBuilder lr = new StringBuilder();
            lr.append('|');
            for (int j = 0; j < m; j++) {
                lr.append(' ');
                if (j == m - 1 || mat[i][j] != mat[i][j + 1]) {
                    lr.append('|');
                } else {
                    lr.append(' ');
                }
            }
            ans.add(lr.toString());
        }
        return ans.stream().toArray(x -> new String[x]);
    }

    public void debug(String[] mat) {
        System.err.println();
        for (String row : mat) {
            System.err.println(row);
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
}
