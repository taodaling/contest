package contest;

import template.algo.IntBinarySearch;
import template.datastructure.MonotoneOrder;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class SultansPearls {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        Peal[] peals = new Peal[n];
        for (int i = 0; i < n; i++) {
            peals[i] = new Peal();
            peals[i].w = in.ri();
            peals[i].c = in.ri();
        }
        SequenceUtils.reverse(peals);
        IntegerPreSum psOfC = new IntegerPreSum(i -> peals[i].c, n);
        IntegerPreSum psOfW = new IntegerPreSum(i -> peals[i].w, n);
        int best = -1;
        int bestL = -1;
        int bestR = -1;
        int sum = psOfC.intervalSum(0, n - 1);
        for (int i = m - 1; i < n; i++) {
            int weight = psOfW.intervalSum(i - m + 1, i);
            int l = i + 1;
            int r = n - 1;
            while (l < r) {
                int mid = (l + r) >> 1;
                if (psOfW.intervalSum(i + 1, mid) * k < weight) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            if (psOfW.intervalSum(i + 1, l) * k >= weight) {
                int cand = sum - psOfC.intervalSum(i - m + 1, r);
                if (cand > best) {
                    best = cand;
                    bestL = i - m + 1;
                    bestR = r;
                }
            } else {
                break;
            }
        }

        out.println(n - (bestR - bestL + 1));
        out.println(best);
        for (int i = 0; i < bestL; i++) {
            out.append('H');
        }
        for (int i = bestR + 1; i < n; i++) {
            out.append('T');
        }
        debug.debug("bestL", bestL);
        debug.debug("bestR", bestR);
        debug.debug("best", best);
    }
}

class Peal {
    int w;
    int c;
}
