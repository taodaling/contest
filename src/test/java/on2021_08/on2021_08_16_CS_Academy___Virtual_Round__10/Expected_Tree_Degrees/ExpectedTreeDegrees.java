package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Expected_Tree_Degrees;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.SumOfFloat;

public class ExpectedTreeDegrees {
    public double sum(double[] ps, int l, int r) {
        if (l > r) {
            return 0;
        }
        double ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        double[] invSuffix = new double[n + 2];
        SumOfFloat sum = new SumOfFloat();
        for (int i = n - 1; i >= 0; i--) {
            if (i > 0)
                sum.add(1d / i);
            invSuffix[i] = sum.sum();
        }
        SumOfFloat ans = new SumOfFloat();
        SumOfFloat precalc = new SumOfFloat();

        for (int i = n - 1; i >= 0; i--) {
            precalc.add(invSuffix[i + 2] / (i + 1));
            ans.add(precalc.sum() * 2);
            ans.add(invSuffix[i + 1]);
            if (i > 0) {
                ans.add(1);
                ans.add(2 * invSuffix[i + 1]);
            }
        }
        out.println(ans.sum());
    }
}
