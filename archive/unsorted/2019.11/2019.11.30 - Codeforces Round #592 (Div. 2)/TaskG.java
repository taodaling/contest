package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();

        long min = (long) (1 + n) * n / 2;
        if (k < min) {
            out.println(-1);
            return;
        }
        int[] p = new int[n + 1];
        int[] q = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            p[i] = q[i] = i;
        }

        int l = 1;
        int r = n;
        while (l < r && min + r - l <= k) {
            min += r - l;
            SequenceUtils.swap(q, l, r);
            l++;
            r--;
        }


        if (l < r) {
            while (l < r && min + r - l > k) {
                r--;
            }
            min += r - l;
            SequenceUtils.swap(q, l, r);
        }

        long ans = 0;
        for(int i = 1; i <= n; i++){
            ans += Math.max(p[i], q[i]);
        }

        out.println(ans);
        for(int i = 1; i <= n; i++){
            out.append(p[i]).append(' ');
        }
        out.println();
        for(int i = 1; i <= n; i++){
            out.append(q[i]).append(' ');
        }
        out.println();
    }
}
