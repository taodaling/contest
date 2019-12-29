package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BInterestingSubarray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 1; i < n; i++) {
            if (Math.abs(a[i] - a[i - 1]) >= 2) {
                out.println("YES");
                out.append(i).append(' ').append(i + 1).append('\n');
                return;
            }
        }
        out.println("NO");
    }
}
