package on2019_10.on2019_10_22_AGC007.TaskC;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d1 = in.readInt();
        int x = in.readInt();
        int[] ds = new int[n * 2];
        ds[0] = d1;
        for (int i = 1; i < n * 2; i++) {
            ds[i] = ds[i - 1] + x;
        }
        double[] el = new double[n];
        double[] er = new double[n];
        el[0] = ds[0];
        for (int i = 1; i < n; i++) {
            el[i] = ds[i * 2] + (el[i - 1] + ds[i * 2 - 1]) / 4.0;
        }
        er[n - 1] = ds[n * 2 - 1];
        for (int i = n - 2; i >= 0; i--) {
            er[i] = ds[i * 2 + 1] + (er[i + 1] + ds[i * 2 + 2]) / 4.0;
        }

        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += el[i] + er[i];
        }

        out.printf("%.15f", sum / 2);
    }
}
