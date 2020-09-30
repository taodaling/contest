package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BOmkarAndInfinityClock {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();

        long[] a = new long[n];
        in.populate(a);
        long max = Arrays.stream(a).max().getAsLong();
        k--;
        for (int i = 0; i < n; i++) {
            a[i] = max - a[i];
        }

        if (k % 2 == 1) {
            max = Arrays.stream(a).max().getAsLong();
            for (int i = 0; i < n; i++) {
                a[i] = max - a[i];
            }
        }

        for(int i = 0; i < n; i++){
            out.append(a[i]).append(' ');
        }
        out.println();
    }
}
