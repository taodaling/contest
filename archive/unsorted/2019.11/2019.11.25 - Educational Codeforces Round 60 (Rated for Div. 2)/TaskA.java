package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int mx = SortUtils.maxOf(a, 0, n - 1);
        int ans = 0;
        int length = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] == mx) {
                length++;
            } else {
                length = 0;
            }
            ans = Math.max(ans, length);
        }
        out.println(ans);
    }
}
