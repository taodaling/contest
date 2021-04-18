package on2021_03.on2021_03_29_Library_Checker.Range_Kth_Smallest1;





import template.datastructure.WaveletTrees;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongArrayList;

public class RangeKthSmallest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] a = in.rl(n);
        LongArrayList all = new LongArrayList(a);
        all.unique();
        for (int i = 0; i < n; i++) {
            a[i] = all.binarySearch(a[i]);
        }
        WaveletTrees wt = new WaveletTrees(a);
        for (int i = 0; i < q; i++) {
            int l = in.ri();
            int r = in.ri() - 1;
            int k = in.ri() + 1;
            int index = wt.kthSmallestIndex(l, r, k);
            long ans = all.get((int) a[index]);
            out.println(ans);
        }
    }
}
