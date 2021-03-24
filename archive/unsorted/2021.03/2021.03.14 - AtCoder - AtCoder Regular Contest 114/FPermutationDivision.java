package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.string.IntArrayIntSequenceAdapter;
import template.string.IntSequence;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FPermutationDivision {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] p = new int[n];
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
            inv[p[i]] = i;
        }
        boolean[] cut = new boolean[n];
        int remainCut = k - 1;
        int remainPos = n - 1;
        DSU dsu = new DSU(n);
        dsu.init();
        boolean curMax = false;
        for (int i = n - 1; i >= 0; i--) {
            if (inv[i] == 0) {
                //first
                continue;
            }
            if (remainCut == remainPos) {
                curMax = true;
                cut[inv[i]] = true;
                remainPos--;
                remainCut--;
                continue;
            }
            if (dsu.find(inv[i] - 1) == dsu.find(0)) {
                //cut
                cut[inv[i]] = true;
                remainCut--;
                remainPos--;
                continue;
            }
            //retain
            remainPos--;
            dsu.merge(inv[i], inv[i] - 1);
        }
        assert remainCut == 0;
        List<IntSequence> list = new ArrayList<>(n);
        int lastCut = 0;
        for (int i = 1; i < n; i++) {
            if (cut[i]) {
                list.add(new IntArrayIntSequenceAdapter(p, lastCut, i - 1));
                lastCut = i;
            }
        }
        list.add(new IntArrayIntSequenceAdapter(p, lastCut, n - 1));
        IntSequence[] data = list.toArray(new IntSequence[0]);
        SortUtils.mergeSort(data, Comparator.naturalOrder(), 0, data.length, new IntSequence[data.length]);
        SequenceUtils.reverse(data);
        for (IntSequence seq : data) {
            for (int i = 0; i < seq.length(); i++) {
                out.append(seq.get(i) + 1).append(' ');
            }
        }
    }
}
