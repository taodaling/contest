package on2021_03.on2021_03_22_Codeforces___Codeforces_Round__243__Div__1_.B__Sereja_and_Table;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class BSerejaAndTable {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }
        int best = (int) 1e9;
        if (n <= k) {
            for (int choice = 0; choice < 1 << n; choice++) {
                int minCost = 0;
                for (int j = 0; j < m; j++) {
                    int c1 = 0;
                    int c0 = 0;
                    for (int i = 0; i < n; i++) {
                        c1 += Bits.get(choice, i) ^ 1 ^ mat[i][j];
                        c0 += Bits.get(choice, i) ^ 0 ^ mat[i][j];
                    }
                    minCost += Math.min(c1, c0);
                }
                best = Math.min(minCost, best);
            }
        } else {
            int[] Y = new int[m];
            for (int choice = 0; choice < n; choice++) {
                for (int t = 0; t < 2; t++) {
                    int minCost = 0;
                    for (int j = 0; j < m; j++) {
                        Y[j] = mat[choice][j] ^ t;
                    }
                    for (int i = 0; i < n; i++) {
                        int c1 = 0;
                        int c0 = 0;
                        for (int j = 0; j < m; j++) {
                            c1 += mat[i][j] ^ 1 ^ Y[j];
                            c0 += mat[i][j] ^ 0 ^ Y[j];
                        }
                        minCost += Math.min(c1, c0);
                    }
                    best = Math.min(minCost, best);
                }
            }
        }

        out.println(best > k ? -1 : best);
    }
}