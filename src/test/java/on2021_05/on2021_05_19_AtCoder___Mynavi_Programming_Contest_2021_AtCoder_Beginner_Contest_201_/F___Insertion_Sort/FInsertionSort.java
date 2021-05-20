package on2021_05.on2021_05_19_AtCoder___Mynavi_Programming_Contest_2021_AtCoder_Beginner_Contest_201_.F___Insertion_Sort;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongGenericBIT;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

public class FInsertionSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        int[] c = new int[n];
        int[] p = new int[n];
        int[] invP = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
            invP[p[i]] = i;
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.ri();
            a[i] = in.ri();
            c[i] = in.ri();

            a[i] = Math.min(a[i], b[i]);
            c[i] = Math.min(c[i], b[i]);
        }
        long inf = Long.MAX_VALUE / 2;
        LongPreSum psA = new LongPreSum(i -> a[i], n);
        LongPreSum psB = new LongPreSum(i -> b[i], n);
        LongPreSum psC = new LongPreSum(i -> c[i], n);
        LongGenericBIT bit = new LongGenericBIT(n, Math::min, inf);
        long ans = inf;
        ans = Math.min(ans, psA.post(0));
        ans = Math.min(ans, psC.post(0));
        for (int i = 0; i < n; i++) {
            debug.debug("bit", bit);
            debug.debug("i", i);
            int j = invP[i];
            long best = bit.query(j) + psB.prefix(i - 1);
            debug.debug("best", best);
            best = Math.min(best, psA.prefix(i - 1));
            ans = Math.min(ans, best + psC.post(i + 1));
            bit.update(j + 1, best - psB.prefix(i));
        }
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
