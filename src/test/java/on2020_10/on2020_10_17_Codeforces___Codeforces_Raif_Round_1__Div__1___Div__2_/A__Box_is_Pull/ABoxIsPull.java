package on2020_10.on2020_10_17_Codeforces___Codeforces_Raif_Round_1__Div__1___Div__2_.A__Box_is_Pull;



import template.io.FastInput;

import java.io.PrintWriter;

public class ABoxIsPull {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long[] a = new long[]{in.readInt(), in.readInt()};
        long[] b = new long[]{in.readInt(), in.readInt()};
        if (a[0] == b[0] && a[1] == b[1]) {
            out.println(0);
            return;
        }
        int extra = a[0] != b[0] && a[1] != b[1] ? 2 : 0;
        out.println(Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + extra);
    }
}
