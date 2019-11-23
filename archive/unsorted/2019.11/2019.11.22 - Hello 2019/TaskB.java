package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        if (find(a, 0, 0)) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }

    public boolean find(int[] a, int i, int val) {
        if (i == a.length) {
            return val % 360 == 0;
        }
        return find(a, i + 1, val + a[i]) || find(a, i + 1, Math.abs(val - a[i]));
    }
}
