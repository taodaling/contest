package on2021_10.on2021_10_17_Library_Checker.Static_Range_Frequency;



import template.datastructure.PersistentTreap;
import template.datastructure.WaveletTrees;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;

public class StaticRangeFrequency {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] a = in.rl(n);
        LongArrayList all = new LongArrayList(a);
        all.unique();
        for(int i = 0; i < n; i++){
            a[i] = all.binarySearch(a[i]);
        }
        WaveletTrees wt = new WaveletTrees(a);
        for(int i = 0; i < q; i++){
            int l = in.ri();
            int r = in.ri() - 1;
            int x = in.ri();
            int occur = all.binarySearch(x);
            if(occur < 0){
                out.println(0);
                continue;
            }
            int ans = wt.range(l, r, occur, occur);
            out.println(ans);
        }
    }
}
