package on2019_11.on2019_11_16_AtCoder_Beginner_Contest_145.C___Average_Length;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        double[][] xy = new double[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                xy[i][j] = in.readInt();
            }
        }

        double avg = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = xy[i][0] - xy[j][0];
                double dy = xy[i][1] - xy[j][1];
                double dist = Math.sqrt(dx * dx + dy * dy);
                avg += dist;
            }
        }

        double ans = avg / (n * (n - 1) / 2);
        ans *= (n - 1);
        out.printf("%.10f", ans);
    }
}
