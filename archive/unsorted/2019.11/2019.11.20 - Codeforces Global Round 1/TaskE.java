package contest;

import template.*;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = in.readInt();
        }
        int[] t = new int[n];
        for (int i = 0; i < n; i++) {
            t[i] = in.readInt();
        }
        int[] dc = new int[n];
        int[] dt = new int[n];
        dc[0] = c[0];
        dt[0] = t[0];
        for (int i = 1; i < n; i++) {
            dc[i] = c[i] - c[i - 1];
            dt[i] = t[i] - t[i - 1];
        }
        if (c[0] != t[0]) {
            out.println("No");
            return;
        }
        Randomized.randomizedArray(dc, 0, n);
        Randomized.randomizedArray(dt, 0, n);
        Arrays.sort(dc);
        Arrays.sort(dt);
        if (CompareUtils.compareArray(dc, 0, n - 1, dt, 0, n - 1) == 0) {
            out.println("Yes");
        } else {
            out.println("No");
        }
    }
}
