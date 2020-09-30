package contest;

import template.primitve.generated.datastructure.IntegerVersionArray;

public class LightbulbGame {
    public int countWinningMoves(String[] board) {
        int n = board.length;
        int m = board[0].length();
        int[][] sg = new int[n + 1][m + 1];
        int[][] mat = new int[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i + 1][j + 1] = board[n - 1 - i].charAt(m - 1 - j) - '0';
            }
        }
        IntegerVersionArray iva = new IntegerVersionArray(100);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                iva.clear();
                for (int k = 0; k < i; k++) {
                    iva.set(sg[k][j], 1);
                }
                for (int k = 0; k < j; k++) {
                    iva.set(sg[i][k], 1);
                }
                while (iva.get(sg[i][j]) == 1) {
                    sg[i][j]++;
                }
            }
        }
        int xor = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                xor ^= sg[i][j];
            }
        }
        int ans = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                for (int k = 0; k < i; k++) {
                    if ((xor ^ sg[i][j] ^ sg[k][j]) == 0) {
                        ans++;
                    }
                }
                for (int k = 1; k < j; k++) {
                    if ((xor ^ sg[i][j] ^ sg[i][k]) == 0) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }
}
