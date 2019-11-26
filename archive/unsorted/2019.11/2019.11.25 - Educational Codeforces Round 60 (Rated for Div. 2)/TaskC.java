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

        long l = 0;
        long r = (long) 1e18;
        while (l < r) {
            long mid = (l + r) >>> 1;
            if (check(src, dst, dxy, mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (check(src, dst, dxy, l)) {
            out.println(l);
        } else {
            out.println(-1);
        }
    }

    public boolean check(long[] src, long[] dst, int[][] dxy, long time) {
        int n = dxy.length;
        long[] sum = new long[2];
        for (int i = 0; i < n; i++) {
            sum[0] += dxy[i][0];
            sum[1] += dxy[i][1];
        }
        long[] trace = src.clone();
        trace[0] += sum[0] * (time / n);
        trace[1] += sum[1] * (time / n);
        for (int i = 0; i < time % n; i++) {
            trace[0] += dxy[i][0];
            trace[1] += dxy[i][1];
        }
        return dist(trace, dst) <= time;
    }

    public long dist(long[] a, long[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }
}
