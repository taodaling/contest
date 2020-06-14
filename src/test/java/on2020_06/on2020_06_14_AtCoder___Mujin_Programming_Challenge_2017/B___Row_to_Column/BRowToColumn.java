package on2020_06.on2020_06_14_AtCoder___Mujin_Programming_Challenge_2017.B___Row_to_Column;



import template.io.FastInput;
import template.io.FastOutput;

public class BRowToColumn {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readChar() == '#' ? 1 : 0;
                sum += mat[i][j];
            }
        }

        if (sum == 0) {
            out.println(-1);
            return;
        }

        int ans = (int) 1e9;
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                cnt += mat[j][i];
            }

            int row = 0;
            for (int j = 0; j < n; j++) {
                row += mat[i][j];
            }

            int req = n - row;
            ans = Math.min(ans, req + (cnt == 0 ? 1 : 0));
        }

        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                cnt += mat[j][i];
            }
            if (cnt < n) {
                ans++;
            }
        }

        out.println(ans);
    }
}
