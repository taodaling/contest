package contest;

import template.binary.CachedLog2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

public class APoweredAddition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        int time = 0;
        long last = a[0];
        for (int i = 1; i < n; i++) {
            if (a[i] >= last) {
                last = a[i];
            } else {
                time = Math.max(time, CachedLog2.floorLog(last - a[i]) + 1);
            }
        }

        out.println(time);
    }
}
