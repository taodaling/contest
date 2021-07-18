package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_2.P5___Majority_Subarrays;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.CountContinuous01Subsequence;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class P5MajoritySubarrays {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.ri();
        }
        int[][] g = SequenceUtils.groupBy(i -> a[i], n, n + 1);
        CountContinuous01Subsequence cc = new CountContinuous01Subsequence(n);
        long ans = 0;
        for(int i = 1; i <= n; i++){
            if(g[i].length == 0){
                continue;
            }
            int[] indices = g[i];
            cc.init(j -> indices[j], indices.length);
            long contrib = cc.countPositive();
            ans += contrib;
            debug.debug("i", i);
            debug.debug("contrib", contrib);
        }
        out.println(ans);
    }
}
