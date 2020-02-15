package on2020_02.on2020_02_15_CodeChef___Practice_Medium_.Funny_gnomes;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedLog2;
import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerDequeImpl;

import java.util.Arrays;
import java.util.BitSet;

public class FunnyGnomes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        BitSet[] edges = new BitSet[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new BitSet(n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i].set(j, in.readInt() == 1);
            }
        }


        BitSet[][] jump = new BitSet[31][n];
        jump[0] = edges;
        for (int i = 1; i <= 30; i++) {
            for (int j = 0; j < n; j++) {
                jump[i][j] = new BitSet(n);
                jump[i][j] = merge(jump[i - 1][j], jump[i - 1]);
            }
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int k = in.readInt();
            int x = in.readInt() - 1;
            BitSet state = new BitSet(n);
            state.set(x);
            while (k > 0) {
                int log = CachedLog2.floorLog(k);
                state = merge(state, jump[log]);
                k -= 1 << log;
            }
            int card = state.cardinality();
            out.println(card);
            if (card == 0) {
                out.println(-1);
            } else {
                for (int j = state.nextSetBit(0); j >= 0; j = state.nextSetBit(j + 1)) {
                    out.append(j + 1).append(' ');
                }
                out.println();
            }
        }
    }

    public BitSet merge(BitSet set, BitSet[] level) {
        BitSet ans = new BitSet();
        for (int i = 0; i < level.length; i++) {
            if (set.get(i)) {
                ans.or(level[i]);
            }
        }
        return ans;
    }
}
