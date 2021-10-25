package on2021_10.on2021_10_19_AtCoder___Daiwa_Securities_Co__Ltd__Programming_Contest_2021_AtCoder_Regular_Contest_128_.C___Max_Dot;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearProgramming;
import template.primitve.generated.datastructure.LongPreSum;

public class CMaxDot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int s = in.ri();
        long[] a = in.rl(n);
        LongPreSum ps = new LongPreSum(i -> a[i], n);
        LinearProgramming lp = new LinearProgramming(2, n, 1e-8);
        for (int i = 1; i <= n; i++) {
            lp.setConstraintCoefficient(1, i, 1);
            lp.setConstraintCoefficient(2, i, n + 1 - i);
            lp.setTargetCoefficient(i, ps.post(i - 1));
        }
        lp.setConstraintConstant(1, m);
        lp.setConstraintConstant(2, s);
        lp.solve();
        double ans = lp.maxSolution();
        out.println(ans);
    }
}
