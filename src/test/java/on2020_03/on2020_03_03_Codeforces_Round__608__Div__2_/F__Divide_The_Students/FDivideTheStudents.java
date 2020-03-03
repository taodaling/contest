package on2020_03.on2020_03_03_Codeforces_Round__608__Div__2_.F__Divide_The_Students;



import template.io.FastInput;
import template.io.FastOutput;

public class FDivideTheStudents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] c1 = new int[4];
        int[] c2 = new int[4];
        for (int i = 1; i < 4; i++) {
            c1[i] = in.readInt();
        }
        for (int i = 1; i < 4; i++) {
            c2[i] = in.readInt();
        }
        int[] f = new int[8];
        for (int i = 1; i < 8; i++) {
            f[i] = in.readInt();
        }

        for (int i = 0; i <= f[2]; i++) {
            for (int j = 0; j <= f[3]; j++) {
                for (int k = 0; k <= f[5]; k++) {
                    int c11 = c1[1] - i - j;
                    int c21 = c2[1] - f[2] - f[3] + i + j;
                    int c12 = c1[2] - i - k;
                    int c22 = c2[2] - f[2] - f[5] + i + k;
                    int c13 = c1[3] - j - k;
                    int c23 = c2[3] - f[3] - f[5] + j + k;
                    if (c11 < 0 || c12 < 0 || c13 < 0 || c21 < 0 || c22 < 0 || c23 < 0) {
                        continue;
                    }
                    int a1 = Math.min(Math.min(c11, c12), c13);
                    int a2 = Math.min(Math.min(c21, c22), c23);
                    if (a1 + a2 < f[1] || c11 + c21 - f[1] - f[4] < 0
                            || c12 + c22 - f[1] - f[6] < 0
                            || c13 + c23 - f[1] - f[7] < 0) {
                        continue;
                    }
                    int g1 = Math.min(a1, f[1]);
                    out.append(g1).append(' ');
                    out.append(i).append(' ');
                    out.append(j).append(' ');
                    out.append(Math.min(f[4], c11 - g1)).append(' ');
                    out.append(k).append(' ');
                    out.append(Math.min(f[6], c12 - g1)).append(' ');
                    out.append(Math.min(f[7], c13 - g1)).append(' ');
                    out.println();
                    return;
                }
            }
        }

        out.println(-1);
    }
}
