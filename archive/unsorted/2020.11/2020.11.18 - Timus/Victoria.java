package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class Victoria {
    int[][] dp;
    int[][] prev;

    void update(int i, int j, int mask, int x) {
        if (dp[i][j] < x) {
            dp[i][j] = x;
            prev[i][j] = mask;
        }
    }

    public List<int[]> getIS(char[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        dp = new int[n * m + 1][1 << (m + 1)];
        prev = new int[n * m + 1][1 << (m + 1)];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0] = 0;
        int mask = (1 << m + 1) - 1;
        for (int i = 0; i + 1 < dp.length; i++) {
            int row = i / m;
            int col = i % m;
            for (int j = 0; j < 1 << m + 1; j++) {
                //not set
                update(i + 1, (j << 1) & mask, j, dp[i][j]);
                //set
                boolean possible = true;
                if (mat[row][col] == '*') {
                    possible = false;
                }
                if (col > 0 && (Bits.get(j, 0) == 1 || Bits.get(j, m) == 1)) {
                    possible = false;
                }
                if (Bits.get(j, m - 1) == 1) {
                    possible = false;
                }
                if (col < m - 1 && Bits.get(j, m - 2) == 1) {
                    possible = false;
                }

                if (possible) {
                    update(i + 1, (j << 1) & mask | 1, j, dp[i][j] + 1);
                }
            }
        }

        int cur = 0;
        for (int i = 0; i < 1 << m + 1; i++) {
            if (dp[n * m][i] > dp[n * m][cur]) {
                cur = i;
            }
        }

        List<int[]> ans = new ArrayList<>();
        for (int i = n * m; i >= 1; i--) {
            int row = (i - 1) / m;
            int col = (i - 1) % m;
            if (Bits.get(cur, 0) == 1) {
                ans.add(new int[]{row, col});
            }
            cur = prev[i][cur];
        }

        debug.debugMatrix("mat", mat);
        debug.debug("dp", dp);
        return ans;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        char[][] A = new char[n][3];
        char[][] B = new char[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                A[i][j] = in.readChar();
            }
            in.readChar();
            in.readChar();
            in.readChar();
            for (int j = 0; j < 3; j++) {
                B[i][j] = in.readChar();
            }
        }

        debug.debugMatrix("A", A);
        debug.debugMatrix("B", B);
        List<int[]> left = getIS(A);
        List<int[]> right = getIS(B);
        for(int[] a : left){
            debug.debugArray("a", a);
        }
        for(int[] a : right){
            debug.debugArray("b", a);
        }
        if (left.size() + right.size() < k) {
            out.println("PORAZHENIE");
            return;
        }
        out.println("POBEDA");
        for (int[] xy : left) {
            if (k == 0) {
                break;
            }
            k--;
            out.append(xy[0] + 1).append((char) ('A' + xy[1])).println();
        }

        for (int[] xy : right) {
            if (k == 0) {
                break;
            }
            k--;
            out.append(xy[0] + 1).append((char) ('D' + xy[1])).println();
        }
    }
}
