package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AMultiplesOfLength {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);
        if (n == 1) {
            out.println("1 1");
            out.println(-a[0]);
            out.println("1 1");
            out.println(0);
            out.println("1 1");
            out.println(0);
            return;
        }

        out.append(1).append(' ').append(n - 1).println();
        for (int i = 0; i < n - 1; i++) {
            long k = a[i];
            out.append(k * (n - 1)).append(' ');
        }
        out.println();
        out.append(n).append(' ').append(n).println();
        out.append(-a[n - 1]).println();
        out.append(1).append(' ').append(n).println();
        for (int i = 0; i < n - 1; i++) {
            long k = a[i];
            out.append(-k * n).append(' ');
        }
        out.append(0).println();
    }
}
