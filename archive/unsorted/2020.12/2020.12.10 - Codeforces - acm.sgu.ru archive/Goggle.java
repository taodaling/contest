package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.List;

public class Goggle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList[] seq = new IntegerArrayList[n];
        BitSet[] contain = new BitSet[n];
        int threshold = 100;
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            seq[i] = new IntegerArrayList(k);
            for (int j = 0; j < k; j++) {
                seq[i].add(in.ri());
            }
            contain[i] = new BitSet(threshold + 1);
            for (int t : seq[i].toArray()) {
                contain[i].set(t);
            }
        }
        BitSet req = new BitSet(threshold + 1);
        BitSet not = new BitSet(threshold + 1);
        BitSet buf = new BitSet(threshold + 1);
        List<IntegerArrayList> match = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int k = in.ri();
            req.fill(false);
            not.fill(false);
            match.clear();
            for (int j = 0; j < k; j++) {
                int x = in.ri();
                if (x > 0) {
                    req.set(x);
                } else {
                    not.set(-x);
                }
            }
            for (int j = 0; j < n; j++) {
                buf.copy(contain[j]);
                buf.and(req);
                if (buf.size() != req.size()) {
                    continue;
                }
                buf.copy(contain[j]);
                buf.and(not);
                if (buf.size() > 0) {
                    continue;
                }
                match.add(seq[j]);
            }
            out.println(match.size());
            for (IntegerArrayList list : match) {
                out.append(list.size()).append(' ');
                for (int x : list.toArray()) {
                    out.append(x).append(' ');
                }
                out.println();
            }
        }
    }
}
