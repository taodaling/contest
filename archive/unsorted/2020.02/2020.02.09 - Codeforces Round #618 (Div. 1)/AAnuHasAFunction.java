package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class AAnuHasAFunction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] pre = new int[n];
        int[] post = new int[n];
        for (int i = 0; i < n; i++) {
            pre[i] = a[i];
            if (i > 0) {
                pre[i] |= pre[i - 1];
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            post[i] = a[i];
            if (i < n - 1) {
                post[i] |= post[i + 1];
            }
        }
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            int merge = 0;
            if (i > 0) {
                merge |= pre[i - 1];
            }
            if (i + 1 < n) {
                merge |= post[i + 1];
            }
            int cur = a[i] - (a[i] & merge);
            if (cur > max) {
                max = cur;
                maxIndex = i;
            }
        }

        SequenceUtils.swap(a, maxIndex, 0);
        for (int x : a) {
            out.append(x).append(' ');
        }
    }
}
