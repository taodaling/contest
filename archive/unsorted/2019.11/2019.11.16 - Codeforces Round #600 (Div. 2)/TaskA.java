package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt();
        }

        int time = 0;
        boolean valid = true;
        for (int i = 0; i < n; i++) {
            valid = valid && a[i] <= b[i];
            if (a[i] != b[i]) {
                if (i > 0 && a[i] - b[i] == a[i - 1] - b[i - 1]) {
                    continue;
                }
                time++;
            }
        }

        out.println(valid && time <= 1 ? "YES" : "NO");
    }
}
