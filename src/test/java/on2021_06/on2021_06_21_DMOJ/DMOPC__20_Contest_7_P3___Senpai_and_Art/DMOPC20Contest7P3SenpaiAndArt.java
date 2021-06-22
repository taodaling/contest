package on2021_06.on2021_06_21_DMOJ.DMOPC__20_Contest_7_P3___Senpai_and_Art;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MinimumSegmentSubtract;
import template.utils.Debug;

public class DMOPC20Contest7P3SenpaiAndArt {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int l = in.ri();
        int r = in.ri();
        int[] a = in.ri(n);
        long ans = MinimumSegmentSubtract.solve(n, l, r, i -> a[i]);
        out.println(ans);
    }
}
