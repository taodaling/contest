package contest;

import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        int[][] mat = new int[n][m];
        if (m % 2 == 1) {
            for (int i = 0; i < n - 1 && b > 0; i += 2) {
                if (n % 2 == 1 && i == 2) {
                    i = 3;
                }
                mat[i][0] = '^';
                mat[i + 1][0] = 'v';
                b--;
            }
        }
        if (n % 2 == 1) {
            for (int i = m % 2; i < m - 1 && a > 0; i += 2) {
                mat[0][i] = '<';
                mat[0][i + 1] = '>';
                a--;
            }
        }

        for (int i = n % 2; i < n - 1; i += 2) {
            for (int j = m % 2; j < m - 1; j += 2) {
                if (i == 1 && j == 1 && a % 2 == 1 && b % 2 == 1) {
                    mat[i + 1][j - 1] = '<';
                    mat[i + 1][j] = '>';
                    mat[i][j + 1] = '^';
                    mat[i + 1][j + 1] = 'v';
                    a--;
                    b--;
                } else if (a > 0) {
                    a--;
                    mat[i][j] = '<';
                    mat[i][j + 1] = '>';
                    if (a > 0) {
                        a--;
                        mat[i + 1][j] = '<';
                        mat[i + 1][j + 1] = '>';
                    }
                } else if (b > 0) {
                    b--;
                    mat[i][j] = '^';
                    mat[i + 1][j] = 'v';
                    if (b > 0) {
                        b--;
                        mat[i][j + 1] = '^';
                        mat[i + 1][j + 1] = 'v';
                    }
                }
            }
        }

        if(a + b != 0){
            out.println("NO");
            return;
        }

        out.println("YES");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append((char)(mat[i][j] == 0 ? '.' : mat[i][j]));
            }
            out.println();
        }
    }
}
