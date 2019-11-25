package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long[] src = new long[]{in.readInt(), in.readInt()};
        long[] dst = new long[]{in.readInt(), in.readInt()};
        int n = in.readInt();
        int[][] dxy = new int[n][2];
        for (int i = 0; i < n; i++) {
            switch (in.readChar()) {
                case 'U':
                    dxy[i][1] = 1;
                    break;
                case 'D':
                    dxy[i][1] = -1;
                    break;
                case 'L':
                    dxy[i][0] = -1;
                    break;
                case 'R':
                    dxy[i][0] = 1;
                    break;
            }
        }
        long[] trace = src.clone();
        long[] sum = new long[2];
        for (int i = 0; i < n; i++) {
            trace[0] += dxy[i][0];
            trace[1] += dxy[i][1];
            sum[0] += dxy[i][0];
            sum[1] += dxy[i][1];
        }

        long shrink = dist(src, dst) - (dist(trace, dst) - n);
        if (shrink == 0) {
            out.println(-1);
            return;
        }
        long loopNeed = dist(src, dst) / shrink;
        long time = loopNeed * n;
        trace[0] = src[0] + sum[0] * loopNeed;
        trace[1] = src[1] + sum[1] * loopNeed;
        for (int i = 0; i < n && dist(trace, dst) > time; i++) {
            trace[0] += dxy[i][0];
            trace[1] += dxy[i][1];
            time++;
        }

        out.println(time);
    }

    public long dist(long[] a, long[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
