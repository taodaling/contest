package on2020_09.on2020_09_06_2020_TopCoder_Open_Algo.CovidCinema;



public class CovidCinema {
    public boolean possible(char[][] mat, int i, int j) {
        if (i < 0 || i >= mat.length || j < 0 || j >= mat[0].length) {
            return false;
        }
        return true;
    }

    public String[] seat(int R, int C, int A, int B) {
        char[][] mat = new char[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                mat[i][j] = '.';
            }
        }
        for (int i = 0; ; i++) {
            boolean find = false;
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                if (possible(mat, j, k) && A > 0) {
                    find = true;
                    A--;
                    mat[j][k] = 'A';
                }
            }
            if(!find){
                break;
            }
        }
        int[][] dirs = new int[][]{
                {-1, 0},
                {1, 0},
                {0, 1},
                {0, -1}
        };
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                boolean valid = true;
                if (mat[i][j] == 'A') {
                    valid = false;
                }
                for (int[] dir : dirs) {
                    int nx = dir[0] + i;
                    int ny = dir[1] + j;
                    if (nx < 0 || nx >= R || ny < 0 || ny >= C) {
                        continue;
                    }
                    if (mat[nx][ny] == 'A') {
                        valid = false;
                    }
                }
                if (!valid) {
                    continue;
                }
                if (B > 0) {
                    B--;
                    mat[i][j] = 'B';
                }
            }
        }
        if (A > 0 || B > 0) {
            return new String[0];
        }


        String[] ans = new String[mat.length];
        for (int i = 0; i < mat.length; i++) {
            ans[i] = String.valueOf(mat[i]);
        }
        return ans;
    }


}
