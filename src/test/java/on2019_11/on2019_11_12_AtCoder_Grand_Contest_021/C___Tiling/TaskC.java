package on2019_11.on2019_11_12_AtCoder_Grand_Contest_021.C___Tiling;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        char[][] mat = new char[n][m];
        if (n % 2 == 1) {
            for (int i = 0; i < m - 1; i++) {
                if (a == 0 || mat[0][i] != 0 || mat[0][i + 1] != 0) {
                    continue;
                }
                mat[0][i] = '<';
                mat[0][i + 1] = '>';
                a--;
            }
        }
        if (m % 2 == 1) {
            for (int i = 0; i < n - 1; i++) {
                if (b == 0 || mat[i][m - 1] != 0 || mat[i + 1][m - 1] != 0) {
                    continue;
                }
                mat[i][m - 1] = '^';
                mat[i + 1][m - 1] = 'v';
                b--;
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                if (mat[i][j] + mat[i + 1][j] + mat[i][j + 1] + mat[i + 1][j + 1] != 0) {
                    continue;
                }
                if (a > 0) {
                    mat[i][j] = '<';
                    mat[i][j + 1] = '>';
                    a--;
                    if (a > 0) {
                        mat[i + 1][j] = '<';
                        mat[i + 1][j + 1] = '>';
                        a--;
                    }
                } else if (b > 0) {
                    mat[i][j] = '^';
                    mat[i + 1][j] = 'v';
                    b--;
                    if (b > 0) {
                        mat[i][j + 1] = '^';
                        mat[i + 1][j + 1] = 'v';
                        b--;
                    }
                }
            }
        }

        if(b > 0 || a > 0) {
            out.println("NO");
            return;
        }
        yes(out, mat);
    }


    public void yes(FastOutput out, char[][] mat) {
        out.println("YES");
        for (char[] r : mat) {
            for (char c : r) {
                if (c == 0) {
                    out.append('.');
                } else {
                    out.append(c);
                }
            }
            out.println();
        }
    }
}
