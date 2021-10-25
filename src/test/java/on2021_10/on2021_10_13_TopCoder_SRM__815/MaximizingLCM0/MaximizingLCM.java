package on2021_10.on2021_10_13_TopCoder_SRM__815.MaximizingLCM0;



import template.math.LCMs;

public class MaximizingLCM {
    public long maximize(int N, long M) {
        long[] sol = new template.problem.MaximizingLCM().maximize(N, M);
        long ans = 1;
        for (long x : sol) {
            ans = LCMs.lcm(x, ans);
        }
        return ans;
    }
}


