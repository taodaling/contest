package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class CKuroniAndImpossibleCalculation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(m);
        if (n > m) {
            out.println(0);
            return;
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int prod = mod.valueOf(1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                prod = mod.mul(prod, Math.abs(a[i] - a[j]));
            }
        }
        out.println(prod);
    }
}
