package on2020_07.on2020_07_12_AtCoder___CODE_FESTIVAL_2017_Final.F___Distribute_Numbers;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class FDistributeNumbers {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = 4;
        int n = k * (k - 1) + 1;
        BitSet[] rows = new BitSet[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new BitSet(n);
        }
        int[] cnts = new int[n];
        BitSet[] merge = new BitSet[n];
        for (int i = 0; i < n; i++) {
            merge[i] = new BitSet(n);
        }
        BitSet forbidden = new BitSet(n);
        for (int i = 0; i < n; i++) {
            int size = 0;
            forbidden.fill(false);
            for (int j = 0; j < n && size < k; j++) {
                if (forbidden.get(j) || cnts[j] == k) {
                    continue;
                }
                cnts[j]++;
                rows[i].set(j);
                forbidden.or(merge[j]);
                size++;
            }
            for (int j = 0; j < n; j++) {
                if (rows[i].get(j)) {
                    merge[j].or(rows[i]);
                }
            }
            debug.debug("rows[i]", rows[i]);
        }

        out.append(n).append(' ').append(k).println();
        for (BitSet bs : rows) {
            for (int i = bs.nextSetBit(0); i < bs.capacity(); i = bs.nextSetBit(i + 1)) {
                out.append(i + 1).append(' ');
            }
            out.println();
        }
    }
}
