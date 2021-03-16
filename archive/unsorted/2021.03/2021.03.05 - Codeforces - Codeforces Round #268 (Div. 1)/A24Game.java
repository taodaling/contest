package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class A24Game {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n < 4) {
            out.println("NO");
            return;
        }
        out.println("YES");
        if ((n - 4) % 2 == 1) {
            for (int i = 6; i <= n; i += 2) {
                out.append(i + 1).append(" - ").append(i).append(" = ").append(1).println();
                out.println("1 * 1 = 1");
            }
            out.println("5 * 4 = 20");
            out.println("20 + 3 = 23");
            out.println("23 + 2 = 25");
            out.println("25 - 1 = 24");
        } else {
            for (int i = 5; i <= n; i += 2) {
                out.append(i + 1).append(" - ").append(i).append(" = ").append(1).println();
                out.println("1 * 1 = 1");
            }
            out.println("1 * 2 = 2");
            out.println("2 * 3 = 6");
            out.println("6 * 4 = 24");
        }
    }
}
