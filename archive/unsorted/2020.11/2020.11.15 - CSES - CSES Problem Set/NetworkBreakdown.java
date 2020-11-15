package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.util.HashSet;
import java.util.Set;

public class NetworkBreakdown {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Set<Long> sets = new HashSet<>(m);
        for (int i = 0; i < m; i++) {
            sets.add(eId(in.readInt() - 1, in.readInt() - 1));
        }
        int[][] qs = new int[k][2];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 2; j++) {
                qs[i][j] = in.readInt() - 1;
            }
            sets.remove(eId(qs[i][0], qs[i][1]));
        }

        int cc = n;
        DSU dsu = new DSU(n);
        dsu.init();
        for (long e : sets) {
            int a = DigitUtils.highBit(e);
            int b = DigitUtils.lowBit(e);
            if (dsu.find(a) != dsu.find(b)) {
                dsu.merge(a, b);
                cc--;
            }
        }

        SequenceUtils.reverse(qs);
        IntegerArrayList ans = new IntegerArrayList(k);
        for (int[] q : qs) {
            int a = q[0];
            int b = q[1];
            ans.add(cc);
            if (dsu.find(a) != dsu.find(b)) {
                dsu.merge(a, b);
                cc--;
            }
        }
        ans.reverse();
        for(int x : ans.toArray()){
            out.println(x);
        }
    }

    long eId(int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        return DigitUtils.asLong(i, j);
    }
}
