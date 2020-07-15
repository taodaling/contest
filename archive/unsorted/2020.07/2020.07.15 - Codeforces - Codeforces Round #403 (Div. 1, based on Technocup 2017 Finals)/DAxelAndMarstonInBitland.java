package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

public class DAxelAndMarstonInBitland {
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        int limit = 60;
        BitSet[][] f = new BitSet[limit + 1][n];
        long[][] fBest = new long[limit + 1][n];
        BitSet[][] g = new BitSet[limit + 1][n];
        long[][] gBest = new long[limit + 1][n];

        for (int i = 0; i <= limit; i++) {
            for (int j = 0; j < n; j++) {
                f[i][j] = new BitSet(n);
                g[i][j] = new BitSet(n);
            }
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int t = in.readInt();
            if (t == 0) {
                f[0][a].set(b);
                fBest[0][a] = 1;
            } else {
                g[0][a].set(b);
                gBest[0][a] = 1;
            }
        }

        int level = 0;
        for (; level < limit && fBest[level][0] == (1L << level); level++) {
            //calc f
            update(f[level + 1], fBest[level + 1], f[level], fBest[level], g[level], gBest[level]);
            update(g[level + 1], gBest[level + 1], g[level], gBest[level], f[level], fBest[level]);
        }

        if (fBest[level][0] > 1e18) {
            out.println(-1);
            return;
        }

        out.println(fBest[level][0]);
    }

    public void update(BitSet[] nextF, long[] nextFBest, BitSet[] f, long[] fBest, BitSet[] g, long[] gBest) {
        for (int i = 0; i < n; i++) {
            nextFBest[i] = fBest[i];
            for (int j = f[i].nextSetBit(0); j < f[i].capacity(); j = f[i].nextSetBit(j + 1)) {
                nextFBest[i] = Math.max(nextFBest[i], fBest[i] + gBest[j]);
                nextF[i].or(g[j]);
            }
        }
    }
}
