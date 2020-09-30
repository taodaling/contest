package contest;

import template.math.ModMatrix;
import template.math.ModSparseMatrix;
import template.math.ModVectorLinearRecurrenceSolver;
import template.math.Modular;
import template.utils.ArrayIndex;
import template.utils.SequenceUtils;

public class LongPalindromes {
    //Debug debug = new Debug(true);

    public int count(int repeats, String pattern) {
        int n = pattern.length();
        sa = pattern.toCharArray();
        sb = reverse(pattern).toCharArray();
        vectors = new int[2][2][n + 1][n + 1][];
        ai = new ArrayIndex(n + 1, 2, 2);
        vectorWith = new int[ai.totalSize()][];

        int[][] mat = new int[ai.totalSize()][];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                mat[ai.indexOf(i, j, 0)] = dp(j, 0, i, 0);
                mat[ai.indexOf(i, j, 1)] = dp(0, j, 0, i);
            }
        }
        //debug.elapse("mat");
        ModMatrix transfer = new ModMatrix(mat);
        //ModMatrix transferN = ModMatrix.pow(transfer, repeats - 1, mod);

        bf = new int[2][2][n][n];
        //debug.debug("size", ai.totalSize());
        //debug.elapse("pow");

        SequenceUtils.deepFill(bf, -1);
        //debug.debug("bf(0, 0, 0, n - 1)", bf(0, 0, 0, n - 1));

        int[] vec = new int[ai.totalSize()];
        ModMatrix initState = new ModMatrix(ai.totalSize(), 1);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                //0
                initState.set(ai.indexOf(i, j, 0), 0, bf(j, 0, i, n - 1));
                //1
                initState.set(ai.indexOf(i, j, 1), 0, bf(0, j, 0, n - 1 - i));
                vec[ai.indexOf(i, j, 0)] = bf(j, 0, i, n - 1);
                vec[ai.indexOf(i, j, 1)] = bf(0, j, 0, n - 1 - i);
            }
        }

        ModVectorLinearRecurrenceSolver solver = new ModVectorLinearRecurrenceSolver(new ModSparseMatrix(transfer),
                vec, mod);
        //debug.elapse("init");
        //ModMatrix finalState = ModMatrix.mul(transferN, initState, mod);
        int[] finalState = solver.solve(repeats - 1);
        int ans = finalState[ai.indexOf(0, 0, 0)];//finalState.get(ai.indexOf(0, 0, 0), 0);

        return ans;
    }

    int[][][][] bf;

    public int bf(int lLock, int rLock, int l, int r) {
        if (l > r) {
            return lLock == 0 && rLock == 0 ? 1 : 0;
        }
        if (l == r) {
            return lLock == 0 && rLock == 0 ? 2 : 1;
        }
        if (bf[lLock][rLock][l][r] == -1) {
            long ans = 0;
            if (lLock == 0 && rLock == 0) {
                //match both
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(1, 0, l, r - 1);
                ans += bf(0, 1, l + 1, r);
                ans += bf(0, 0, l + 1, r - 1);
            } else if (lLock == 1) {
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(1, 0, l, r - 1);
            } else if (rLock == 1) {
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(0, 1, l + 1, r);
            }
            bf[lLock][rLock][l][r] = (int) (ans % modVal);
        }
        return bf[lLock][rLock][l][r];
    }

    Modular mod = new Modular(1e9 + 7);
    int modVal = mod.getMod();
    ArrayIndex ai;
    int[][][][][] vectors;
    char[] sa;
    char[] sb;
    int[][] vectorWith;

    public int[] vectorWith(int i) {
        if (vectorWith[i] == null) {
            vectorWith[i] = new int[ai.totalSize()];
            vectorWith[i][i] = 1;
        }
        return vectorWith[i];
    }

    public void plus(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (a[i] + b[i]) % modVal;
        }
    }

    public int aChar(int i) {
        return i >= sa.length ? '1' : sa[i];
    }

    public int bChar(int i) {
        return i >= sb.length ? '2' : sb[i];
    }

    public int[] dp(int aLock, int bLock, int a, int b) {
        if (a == sa.length) {
            return vectorWith(ai.indexOf(b, bLock, 1));
        }
        if (b == sb.length) {
            return vectorWith(ai.indexOf(a, aLock, 0));
        }
        if (vectors[aLock][bLock][a][b] == null) {
            int[] vec = vectors[aLock][bLock][a][b] = new int[ai.totalSize()];
            if (aLock == 0 && bLock == 0) {
                //lock both
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //release both
                plus(vec, dp(0, 0, a + 1, b + 1));
                //lock one
                plus(vec, dp(1, 0, a, b + 1));
                plus(vec, dp(0, 1, a + 1, b));
            } else if (aLock == 1) {
                //lock right
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //skip right
                plus(vec, dp(1, 0, a, b + 1));
            } else if (bLock == 1) {
                //lock right
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //skip right
                plus(vec, dp(0, 1, a + 1, b));
            }
        }
        return vectors[aLock][bLock][a][b];
    }

    public String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

}
