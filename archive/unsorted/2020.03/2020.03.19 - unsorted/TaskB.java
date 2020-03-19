package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] b = new long[n];
        for(int i = 0; i < n; i++){
            b[i] = in.readLong();
        }
        long[] a = new long[n];
        long max = 0;
        for(int i = 0; i < n; i++){
            a[i] = b[i] + max;
            max = Math.max(max, a[i]);
        }
        for(int i = 0; i < n; i++){
            out.append(a[i]).append(' ');
        }
    }
}
