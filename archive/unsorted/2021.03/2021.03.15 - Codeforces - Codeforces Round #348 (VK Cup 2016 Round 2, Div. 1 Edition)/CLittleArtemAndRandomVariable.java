package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CLittleArtemAndRandomVariable {
    double prec = 1e-6;

    public double[] solve(double sum, double prod) {
        double a = 1;
        double b = -sum;
        double c = prod;
        double sqrt = Math.sqrt(b * b - 4 * a * c);
        double x1 = (-b + sqrt) / 2 / a;
        double x2 = (-b - sqrt) / 2 / a;
        assert Math.abs(x1 + x2 - sum) <= prec;
        assert Math.abs(x1 * x2 - prod) <= prec;
        assert x1 >= -prec && x2 >= -prec;
        return new double[]{x1, x2};
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        double[] min = new double[n];
        double[] max = new double[n];
        for (int i = 0; i < n; i++) {
            max[i] = in.rd();
        }
        for (int i = 0; i < n; i++) {
            min[i] = in.rd();
        }
        //leq
        for (int i = 1; i < n; i++) {
            max[i] += max[i - 1];
        }
        //geq
        for (int i = n - 2; i >= 0; i--) {
            min[i] += min[i + 1];
        }
        double[] A = new double[n];
        double[] B = new double[n];
        A[n - 1] = 1;
        B[n - 1] = 1;
        for (int i = 0; i < n - 1; i++) {
            double AMulB = max[i];
            double APlusB = AMulB + 1 - min[i + 1];
            double[] sol = solve(APlusB, AMulB);
            Arrays.sort(sol);
            A[i] = sol[0];
            B[i] = sol[1];
        }
        for (int i = n - 1; i >= 1; i--) {
            A[i] -= A[i - 1];
            B[i] -= B[i - 1];
        }
        for (int i = 0; i < n; i++) {
            assert A[i] >= -prec;
            assert B[i] >= -prec;
        }
        for (int i = 0; i < n; i++) {
            out.append(A[i]).append(' ');
        }
        out.println();
        for (int i = 0; i < n; i++) {
            out.append(B[i]).append(' ');
        }
    }
}
