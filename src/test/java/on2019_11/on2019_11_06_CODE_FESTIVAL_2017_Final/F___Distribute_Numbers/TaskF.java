package on2019_11.on2019_11_06_CODE_FESTIVAL_2017_Final.F___Distribute_Numbers;



import template.FastInput;
import template.FastOutput;
import template.GravityModLagrangeInterpolation;
import template.NumberTheory;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.readInt();
        NumberTheory.Modular mod = new NumberTheory.Modular(p);
        GravityModLagrangeInterpolation interpolation = new GravityModLagrangeInterpolation(mod, p);
        for (int i = 0; i < p; i++) {
            interpolation.addPoint(i, in.readInt());
        }
        GravityModLagrangeInterpolation.Polynomial polynomial = interpolation.preparePolynomial();
        for (int i = 0; i < p; i++) {
            out.append(polynomial.getCoefficient(i)).append(' ');
        }
    }
}
