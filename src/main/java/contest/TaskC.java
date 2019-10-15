package contest;

import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] abs = new int[n][2];
        for (int i = 0; i < n; i++) {
            for(int j = 0; j < 2; j++){
                abs[i][j] = in.readInt();
            }
        }
        long x = abs[0][0];
        long y = abs[0][1];

        for(int i = 1; i < n; i++){
            int a = abs[i][0];
            int b = abs[i][1];

            long k = Math.max((x + a - 1) / a, (y + b - 1) / b);
            x = a * k;
            y = b * k;
        }

        out.println(x + y);
    }
}
