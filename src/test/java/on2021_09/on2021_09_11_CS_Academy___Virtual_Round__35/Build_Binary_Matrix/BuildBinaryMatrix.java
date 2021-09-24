package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Build_Binary_Matrix;



import template.binary.Bits;
import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BuildBinaryMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] r = in.ri(n);
        int[] c = in.ri(m);
        Map<Integer, List<Integer>> groupingR = IntStream.range(0, n)
                .boxed().collect(Collectors.groupingBy(x -> r[x]));
        Map<Integer, List<Integer>> groupingC = IntStream.range(0, m)
                .boxed().collect(Collectors.groupingBy(x -> c[x]));
        List<List<Integer>> groupingRValues = new ArrayList<>(groupingR.values());
        List<List<Integer>> groupingCValues = new ArrayList<>(groupingC.values());
        int rNum = groupingRValues.size();
        int cNum = groupingCValues.size();

        BitSet[] ans = compute(rNum, cNum);
        if (ans == null) {
            out.println(-1);
            return;
        }

        int[] invR = new int[n];
        int[] invC = new int[m];
        for (int i = 0; i < rNum; i++) {
            for (int index : groupingRValues.get(i)) {
                invR[index] = i;
            }
        }
        for (int i = 0; i < cNum; i++) {
            for (int index : groupingCValues.get(i)) {
                invC[index] = i;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                boolean v = ans[invR[i]].get(invC[j]);
                out.append(v ? 1 : 0);
            }
            out.println();
        }
    }

    public BitSet next(BitSet s) {
        s = s.clone();
        for (int i = 0; ; i++) {
            if (!s.get(i)) {
                s.set(i);
                return s;
            } else {
                s.clear(i);
            }
        }
    }

    public BitSet[] compute(int r, int c) {
        BitSet[] ans = new BitSet[r];
        if (c < 30 && (1L << c) < r) {
            return null;
        }
        int clog = 1;
        for (int i = 0; i < 30; i++) {
            if (Bits.get(c - 1, i) == 1) {
                clog = Math.max(clog, i + 1);
            }
        }
        if (r < clog) {
            return null;
        }
        Set<BitSet> sets = new HashSet<>();
        for (int i = 0; i < clog; i++) {
            BitSet bs = new BitSet(c);
            for (int j = 0; j < c; j++) {
                if (Bits.get(j, i) == 1) {
                    bs.set(j);
                }
            }
            sets.add(bs);
            ans[i] = bs;
        }
        BitSet iter = new BitSet(c);
        for (int i = clog; i < r; i++) {
            while (sets.contains(iter)) {
                iter = next(iter);
            }
            ans[i] = iter;
            sets.add(iter);
        }
        return ans;
    }
}
