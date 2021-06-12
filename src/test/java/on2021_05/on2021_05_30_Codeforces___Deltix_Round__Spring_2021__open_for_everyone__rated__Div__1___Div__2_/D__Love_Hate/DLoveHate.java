package on2021_05.on2021_05_30_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.D__Love_Hate;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;

import java.util.Arrays;

public class DLoveHate {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int p = in.ri();
        long[] bits = new long[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (in.rc() == '1') {
                    bits[i] |= 1L << j;
                }
            }
        }
        int[] sets = new int[1 << p];
        int[] bc = new int[1 << p];
        for (int i = 0; i < 1 << p; i++) {
            bc[i] = Integer.bitCount(i);
        }
        Randomized.shuffle(bits);
        int req = (n + 1) / 2;
        long best = 0;
        int bestSize = 0;
        long end = System.currentTimeMillis() + 2000;
        for (long bit : bits) {
            if (System.currentTimeMillis() > end) {
                break;
            }
            Arrays.fill(sets, 0);
            IntegerArrayList offsetList = new IntegerArrayList(p);
            for (int i = 0; i < m; i++) {
                if (Bits.get(bit, i) == 1) {
                    offsetList.add(i);
                }
            }
            int[] offsets = offsetList.toArray();
            for (int i = 0; i < n; i++) {
                int v = 0;
                for (int j = 0; j < offsets.length; j++) {
                    if (Bits.get(bits[i], offsets[j]) == 1) {
                        v |= 1 << j;
                    }
                }
                sets[v]++;
            }

            FastWalshHadamardTransform.andFWT(sets, 0, sets.length - 1);
            for (int i = 0; i < sets.length; i++) {
                if (sets[i] >= req && bc[i] > bestSize) {
                    bestSize = bc[i];
                    best = 0;
                    for (int j = 0; j < offsets.length; j++) {
                        if (Bits.get(i, j) == 1) {
                            best |= 1L << offsets[j];
                        }
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            out.append(Bits.get(best, i));
        }
        out.println();
    }
}
