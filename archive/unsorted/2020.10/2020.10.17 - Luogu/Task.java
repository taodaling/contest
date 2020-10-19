package contest;

import template.io.FastInput;
import template.math.PrimitiveRoot;

import java.io.PrintWriter;

public class Task {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int[] p = new int[]{469762049, 998244353, 167772161};
        for (int x : p) {
            out.println(PrimitiveRoot.findAnyRoot(x));
            out.println(log2(x - 1));
        }
    }

    public int log2(int x) {
        if (x % 2 == 1) {
            return 0;
        }
        return log2(x / 2) + 1;
    }
}
