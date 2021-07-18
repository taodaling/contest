package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P4___Hungry_Pigeons;



import template.datastructure.WaveletTrees;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class P4HungryPigeons {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] b = in.ri(n);
        int[] d = in.ri(m);
        Randomized.shuffle(b);
        Arrays.sort(b);
        int[] sortedIndices = IntStream.range(0, m).toArray();
        SortUtils.quickSort(sortedIndices, (i, j) -> Integer.compare(d[i], d[j]), 0, m);
        int[] end = new int[n];
        for (int i = 0, j = -1; i < n; i++) {
            while (j + 1 < m && d[sortedIndices[j + 1]] < b[i]) {
                j++;
            }
            end[i] = j;
        }
        debug.debug("b", b);
        debug.debug("end", end);
        debug.debug("sortedIndices", sortedIndices);

        WaveletTrees wt;
        {
            long[] val = new long[m];
            for (int i = 0; i < m; i++) {
                val[i] = sortedIndices[i];
            }
            wt = new WaveletTrees(val);
        }

        int[] support = new int[m];
        int maxPossible = m / n;
        for (int i = 1; i <= maxPossible; i++) {
            int atLeast = 0;
            for (int j = 0; j < n; j++) {
                int kth = (j + 1) * i;
                if (end[j] + 1 < kth) {
                    atLeast = m;
                    break;
                }
                int cand = sortedIndices[wt.kthSmallestIndex(0, end[j], kth)];
                atLeast = Math.max(cand, atLeast);
            }
            if (atLeast < m) {
                support[atLeast] = Math.max(support[atLeast], i);
            }
        }
        for(int i = 1; i < m; i++){
            support[i] = Math.max(support[i - 1], support[i]);
        }
        for(int i = 0; i < m; i++){
            out.append(support[i]).append(' ');
        }
    }
}
