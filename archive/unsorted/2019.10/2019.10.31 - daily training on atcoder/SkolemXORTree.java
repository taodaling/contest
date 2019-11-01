package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class SkolemXORTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (Integer.bitCount(n) == 1) {
            out.println("No");
            return;
        }
        out.println("Yes");
        DigitUtils.Log2 log2 = new DigitUtils.Log2();
        int floorLog = log2.floorLog(n + 1);

        int m = (1 << floorLog);
        for (int i = 2; i < m; i++) {
            printEdge(out, i - 1, i);
        }
        printEdge(out, m - 1, n + 1);
        for (int i = 2; i < m; i++) {
            printEdge(out, n + i - 1, n + i);
        }


        if(m <= n) {
            int since = m;
            int until = n;
            if (DigitUtils.isOdd(until - since + 1)) {
                until--;
            }

            for (int i = since + 1; i <= until; i++) {
                printEdge(out, i - 1, i);
                printEdge(out, i - 1 + n, i + n);
            }
            printEdge(out, until - (m - 1), until);
            printEdge(out, m - 1, n + since);

            if (until == n - 1) {
                printEdge(out, n, until);
                printEdge(out, n + n, until - (m - 1) - 1);
            }
        }
    }

    public void printEdge(FastOutput out, int a, int b) {
        out.append(a).append(' ').append(b).append('\n');
    }
}
