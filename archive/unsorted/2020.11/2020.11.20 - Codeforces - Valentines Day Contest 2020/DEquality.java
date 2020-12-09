package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DEquality {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int[] a = new int[x];
        int[] b = new int[x];
        for (int i = 0; i < x; i++) {
            a[i] = in.ri();
            b[i] = in.ri();
        }
        int y = in.ri();
        int[] c = new int[y];
        int[] d = new int[y];
        for (int i = 0; i < y; i++) {
            c[i] = in.ri();
            d[i] = in.ri();
        }
    }
}
