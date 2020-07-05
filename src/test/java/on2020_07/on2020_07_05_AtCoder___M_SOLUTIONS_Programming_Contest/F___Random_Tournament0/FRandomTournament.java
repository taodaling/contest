package on2020_07.on2020_07_05_AtCoder___M_SOLUTIONS_Programming_Contest.F___Random_Tournament0;





import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.beans.beancontext.BeanContext;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public class FRandomTournament {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        beats = new BitSet[n];
        for (int i = 0; i < n; i++) {
            beats[i] = new BitSet(n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (in.readChar() == '1') {
                    beats[i].set(j);
                } else {
                    beats[j].set(i);
                }
            }
        }

        L = new BitSet[2][n];
        R = new BitSet[2][n];
        G = new BitSet[2][n];
        LVisited = new int[2][n];
        RVisited = new int[2][n];
        GVisited = new int[2][n];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                L[i][j] = new BitSet(n);
                R[i][j] = new BitSet(n);
                G[i][j] = new BitSet(n);
                RVisited[i][j] = GVisited[i][j] = LVisited[i][j] = i == 0 ? j - 1 : j + 1;
            }
        }
        tmp = new BitSet(n);

        for(int i = 0; i < n; i++){
            prepareL(0, i, n - 1);
            prepareR(0, i, n - 1);
            prepareG(0, i, n - 1);
            prepareL(1, 0, i);
            prepareR(1, 0, i);
            prepareG(1, 0, i);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (R[0][0].get(i) && L[0][i].get(n - 1)) {
                debug.debug("i", i);
                ans++;
            }
        }

        out.println(ans);
    }

    public void prepareL(int i, int j, int k) {
        int step = i == 0 ? 1 : -1;
        int use = i == 0 ? j : k;
        IntPredicate breakCondition = i == 0 ? x -> x < k : x -> x > j;
        IntConsumer consumer = i == 0 ? x -> L[0][j].set(x, L(j, x)) : x -> L[1][k].set(x, L(x, k));
        while (breakCondition.test(LVisited[i][use])) {
            LVisited[i][use] += step;
            consumer.accept(LVisited[i][use]);
        }
    }

    public void prepareR(int i, int j, int k) {
        int step = i == 0 ? 1 : -1;
        int use = i == 0 ? j : k;
        IntPredicate breakCondition = i == 0 ? x -> x < k : x -> x > j;
        IntConsumer consumer = i == 0 ? x -> R[0][j].set(x, R(j, x)) : x -> R[1][k].set(x, R(x, k));
        while (breakCondition.test(RVisited[i][use])) {
            RVisited[i][use] += step;
            consumer.accept(RVisited[i][use]);
        }
    }

    public void prepareG(int i, int j, int k) {
        int step = i == 0 ? 1 : -1;
        int use = i == 0 ? j : k;
        IntPredicate breakCondition = i == 0 ? x -> x < k : x -> x > j;
        IntConsumer consumer = i == 0 ? x -> G[0][j].set(x, G(j, x)) : x -> G[1][k].set(x, G(x, k));
        while (breakCondition.test(GVisited[i][use])) {
            GVisited[i][use] += step;
            consumer.accept(GVisited[i][use]);
        }
    }

    public boolean L(int l, int r) {
        if (l == r) {
            return true;
        }
        prepareL(1, l + 1, r);
        prepareG(0, l, r);
        tmp.copy(beats[l]);
        tmp.and(L[1][r]);
        tmp.and(G[0][l]);
        return tmp.size(l, r) > 0;
    }

    public boolean R(int l, int r) {
        if (l == r) {
            return true;
        }
        prepareR(0, l, r - 1);
        prepareG(1, l, r);
        tmp.copy(beats[r]);
        tmp.and(R[0][l]);
        tmp.and(G[1][r]);
        return tmp.size(l, r) > 0;
    }

    public boolean G(int l, int r) {
        if (l == r) {
            return false;
        }
        prepareL(0, l, r - 1);
        prepareR(1, l + 1, r);
        tmp.copy(R[1][r]);
        tmp.leftShift(1);
        tmp.and(L[0][l]);
        return tmp.size(l, r - 1) > 0;
    }

    BitSet tmp;
    BitSet[] beats;
    BitSet[][] L;
    BitSet[][] R;
    BitSet[][] G;
    int[][] LVisited;
    int[][] RVisited;
    int[][] GVisited;
}
