package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class ChessboardAndQueens {
    char[][] mat = new char[8][8];
    int[] col = new int[8];
    int[] diag1 = new int[20];
    int[] diag2 = new int[20];

    public int bf(int i) {
        if (i >= 8) {
            return 1;
        }
        int ans = 0;
        for (int j = 0; j < 8; j++) {
            int d1 = i - j + 7;
            int d2 = i + j;
            if (mat[i][j] == '*' || col[j] > 0 || diag1[d1] > 0 || diag2[d2] > 0) {
                continue;
            }
            col[j]++;
            diag1[d1]++;
            diag2[d2]++;

            ans += bf(i + 1);

            col[j]--;
            diag1[d1]--;
            diag2[d2]--;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        for (int i = 0; i < 8; i++) {
            in.readString(mat[i], 0);
        }
        int ans = bf(0);
        out.println(ans);
    }
}
