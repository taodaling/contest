package on2020_09.on2020_09_30_Codeforces___Grakn_Forces_2020.A__Circle_Coloring;



import template.io.FastInput;

import java.io.PrintWriter;

public class ACircleColoring {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        int[] c = new int[n];
        in.populate(a);
        in.populate(b);
        in.populate(c);

        int last = 0;
        int first = -1;
        for (int i = 0; i < n; i++) {
            if (a[i] != first && a[i] != last) {
                last = a[i];
            } else if (b[i] != first && b[i] != last) {
                last = b[i];
            } else {
                last = c[i];
            }
            if (i == 0) {
                first = last;
            }

            out.print(last);
            out.print(' ');
        }
        out.println();
    }
}
