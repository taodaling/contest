package on2020_10.on2020_10_23_AtCoder___AtCoder_Beginner_Contest_164.F___I_hate_Matrix_Construction;



import template.binary.Bits;
import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class FIHateMatrixConstruction {
    public long intersect(long a, long b) {
        if (a == -1) {
            return b;
        }
        if (b == -1) {
            return a;
        }
        return a & b;
    }

    public long union(long a, long b) {
        if (a == -1) {
            return b;
        }
        if (b == -1) {
            return a;
        }
        return a | b;
    }

    public boolean solve(int[] rl, int[] rh, int[] cl, int[] ch, int[][] mat) {
        for (int i = 0; i < n; i++) {
            if (rl[i] == 1) {
                for (int j = 0; j < n; j++) {
                    mat[i][j] = 1;
                }
            }
            if (cl[i] == 1) {
                for (int j = 0; j < n; j++) {
                    mat[j][i] = 1;
                }
            }
        }
        int[] rsum = new int[n];
        int[] csum = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rsum[i] += mat[i][j];
                csum[j] += mat[i][j];
            }
        }
        int[] rAllow = new int[n];
        int[] cAllow = new int[n];
        for (int i = 0; i < n; i++) {
            if (rh[i] != -1) {
                rAllow[i] = rh[i] * 1000;
            } else {
                rAllow[i] = n - 1 - rsum[i];
            }
        }
        for (int i = 0; i < n; i++) {
            if (ch[i] != -1) {
                cAllow[i] = ch[i] * 1000;
            } else {
                cAllow[i] = n - 1 - csum[i];
            }
        }
        for (int i = 0; i < n; i++) {
            if (rh[i] <= 0) {
                continue;
            }
            if (rsum[i] > 0) {
                continue;
            }
            boolean find = false;
            for (int j = 0; j < n && !find; j++) {
                if (cAllow[j] > 0) {
                    cAllow[j]--;
                    find = true;
                    mat[i][j] = 1;
                }
            }
            if (!find) {
                return false;
            }
        }
        for (int i = 0; i < n; i++) {
            if (ch[i] <= 0) {
                continue;
            }
            if (csum[i] > 0) {
                continue;
            }
            boolean find = false;
            for (int j = 0; j < n && !find; j++) {
                if (rAllow[j] > 0) {
                    rAllow[j]--;
                    find = true;
                    mat[j][i] = 1;
                }
            }
            if (!find) {
                return false;
            }
        }

        for (int i = 0; i < n; i++) {
            int intersect = 1;
            int union = 0;
            for (int j = 0; j < n; j++) {
                intersect &= mat[i][j];
                union |= mat[i][j];
            }
            if (rl[i] != -1 && rl[i] != intersect) {
                return false;
            }
            if (rh[i] != -1 && rh[i] != union) {
                return false;
            }
        }

        for (int i = 0; i < n; i++) {
            int intersect = 1;
            int union = 0;
            for (int j = 0; j < n; j++) {
                intersect &= mat[j][i];
                union |= mat[j][i];
            }
            if (cl[i] != -1 && cl[i] != intersect) {
                return false;
            }
            if (ch[i] != -1 && ch[i] != union) {
                return false;
            }
        }

        return true;
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        n = in.readInt();
        int[] s = new int[n];
        int[] t = new int[n];
        long[] u = new long[n];
        long[] v = new long[n];
        in.populate(s);
        in.populate(t);
        in.populate(u);
        in.populate(v);

        int[] rl = new int[n];
        int[] rh = new int[n];
        int[] cl = new int[n];
        int[] ch = new int[n];
        long[][] mat = new long[n][n];
        int[][] buf = new int[n][n];
        for (int i = 0; i < 64; i++) {
            Arrays.fill(rl, -1);
            Arrays.fill(rh, -1);
            Arrays.fill(cl, -1);
            Arrays.fill(ch, -1);
            for (int j = 0; j < n; j++) {
                if (s[j] == 0) {
                    rl[j] = Bits.get(u[j], i);
                } else {
                    rh[j] = Bits.get(u[j], i);
                }
                if (t[j] == 0) {
                    cl[j] = Bits.get(v[j], i);
                } else {
                    ch[j] = Bits.get(v[j], i);
                }
            }

            SequenceUtils.deepFill(buf, 0);
            if (!solve(rl, rh, cl, ch, buf)) {
                out.println(-1);
                return;
            }
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (buf[j][k] == 1) {
                        mat[j][k] |= 1L << i;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.print(Long.toUnsignedString(mat[i][j]));
                out.append(' ');
            }
            out.println();
        }
    }


    int n;

    public int left(int i) {
        return i;
    }

    public int right(int i) {
        return n + i;
    }

    public int src() {
        return n + n;
    }

    public int dst() {
        return n + n + 1;
    }
}
