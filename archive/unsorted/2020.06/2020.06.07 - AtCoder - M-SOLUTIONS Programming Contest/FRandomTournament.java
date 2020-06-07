package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Buffer;
import template.utils.SequenceUtils;

public class FRandomTournament {
    BitSet[] adj;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        adj = new BitSet[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new BitSet(n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (in.readChar() == '0') {
                    adj[j].set(i);
                } else {
                    adj[i].set(j);
                }
            }
        }

        lWinner = new BitSet[n];
        rWinner = new BitSet[n];
        lWinnerR = new int[n];
        rWinnerL = new int[n];
        dpL = new int[n][n];
        dpR = new int[n][n];
        SequenceUtils.deepFill(dpL, -1);
        SequenceUtils.deepFill(dpR, -1);
        for (int i = 0; i < n; i++) {
            lWinner[i] = new BitSet(n);
            rWinner[i] = new BitSet(n);
            lWinner[i].set(i);
            rWinner[i].set(i);
            lWinnerR[i] = rWinnerL[i] = i;
            dpL[i][i] = dpR[i][i] = 1;
        }

        buffer = new Buffer<>(() -> new BitSet(n));

        for (int i = 0; i < n; i++) {
            solveLToR(i, n - 1);
        }

        BitSet possible = winner(0, n - 1);
        int ans = possible.size();
        out.println(ans);
    }

    BitSet[] lWinner;
    BitSet[] rWinner;
    int[] lWinnerR;
    int[] rWinnerL;
    Buffer<BitSet> buffer;
    int[][] dpL;
    int[][] dpR;

    public BitSet winner(int l, int r) {
        solveLToR(l, r);
        solveRToL(l, r);
        BitSet set = buffer.alloc();
        set.copy(lWinner[l]);
        set.and(rWinner[r]);
        return set;
    }

    public void solveLToR(int l, int r) {
        while (lWinnerR[l] < r) {
            ++lWinnerR[l];
            if (dpR(l, lWinnerR[l]) == 1) {
                lWinner[l].set(lWinnerR[l]);
            }
        }
    }

    public void solveRToL(int l, int r) {
        while (rWinnerL[r] > l) {
            --rWinnerL[r];
            if (dpL(rWinnerL[r], r) == 1) {
                rWinner[r].set(rWinnerL[r]);
            }
        }
    }

    public int dpR(int l, int r) {
        if (dpR[l][r] == -1) {
            BitSet bs = winner(l, r - 1);
            bs.and(adj[r]);
            dpR[l][r] = Integer.signum(bs.size(l, r - 1));
            buffer.release(bs);
        }
        return dpR[l][r];
    }

    public int dpL(int l, int r) {
        if (dpL[l][r] == -1) {
            BitSet bs = winner(l + 1, r);
            bs.and(adj[l]);
            dpL[l][r] = Integer.signum(bs.size(l + 1, r));
            buffer.release(bs);
        }
        return dpL[l][r];
    }
}
