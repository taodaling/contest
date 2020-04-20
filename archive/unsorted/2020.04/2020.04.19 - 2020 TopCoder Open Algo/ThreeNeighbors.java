package contest;

import java.util.Arrays;

public class ThreeNeighbors {
    public String[] construct(int N) {
        int n = 50;
        char[][] mat = new char[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(mat[i], '.');
        }

        for (int i = 0; i < n && N > 0; i += 3) {
            int l = 0;
            int r = Math.min(n, N + 1);
            for (int j = l; j <= r; j++) {
                mat[i][j] = 'X';
            }
            N -= r - l - 1;
        }

        String[] ans = new String[n];
        for(int i = 0; i < n; i++){
            ans[i] = new String(mat[i]);
        }
        return ans;
    }
}
