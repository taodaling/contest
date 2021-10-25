package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P1___Bosses;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class P1Bosses {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long h = in.ri();
        long p = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        LongPreSum ps = new LongPreSum(i -> a[i], n);

        long best = ps.post(0) * p;
        for (int i = 0; i < n; i++) {
            long cand = 0;
            cand += h * a[i];
            cand += p * (ps.post(i + 1) - (long) (n - (i + 1)) * a[i]);
            best = Math.min(best, cand);
        }

        out.println(best);
    }
}
