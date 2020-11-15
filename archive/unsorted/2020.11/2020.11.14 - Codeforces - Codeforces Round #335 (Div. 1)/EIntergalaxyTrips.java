package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class EIntergalaxyTrips {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        double[][] prob = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                prob[i][j] = in.readInt() / 100d;
            }
        }
        double[] exp = new double[n];
        double[] sum = new double[n];
        double[] remainP = new double[n];
        Arrays.fill(exp, 1e18);
        Arrays.fill(sum, 1);
        Arrays.fill(remainP, 1);
        exp[n - 1] = 0;
        sum[n - 1] = 0;
        remainP[n - 1] = 0;
        boolean[] added = new boolean[n];
        for (int i = 0; i < n; i++) {
            int index = -1;
            for (int j = 0; j < n; j++) {
                if (index == -1 || !added[j] && exp[j] < exp[index]) {
                    index = j;
                }
            }
            if (index < 0) {
                break;
            }
            added[index] = true;
            for (int j = 0; j < n; j++) {
                if (added[j]) {
                    continue;
                }
                sum[j] += remainP[j] * prob[j][index] * exp[index];
                remainP[j] *= 1 - prob[j][index];
                if (remainP[j] == 1) {
                    exp[j] = 1e18;
                } else {
                    exp[j] = sum[j] / (1 - remainP[j]);
                }
            }
        }

        double ans = exp[0];
        out.println(ans);
    }
}
