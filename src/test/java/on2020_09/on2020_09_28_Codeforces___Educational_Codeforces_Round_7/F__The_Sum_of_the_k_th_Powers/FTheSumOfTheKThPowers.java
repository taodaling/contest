package on2020_09.on2020_09_28_Codeforces___Educational_Codeforces_Round_7.F__The_Sum_of_the_k_th_Powers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.polynomial.ModContinuousInterpolation;

public class FTheSumOfTheKThPowers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = (int) (1e9 + 7);
        int n = in.readInt();
        int k = in.readInt();

        if (k == 0) {
            out.println(n);
            return;
        }

        Power power = new Power(mod);
        int[] y = new int[k + 2];
        for (int i = 0; i <= k + 1; i++) {
            y[i] = power.pow(i, k);
        }
        for (int i = 1; i <= k + 1; i++) {
            y[i] = DigitUtils.modplus(y[i - 1], y[i], mod);
        }

        ModContinuousInterpolation interpolation = new ModContinuousInterpolation(0, y, mod);

        int ans = interpolation.interpolate(n);
        out.println(ans);
    }
}
