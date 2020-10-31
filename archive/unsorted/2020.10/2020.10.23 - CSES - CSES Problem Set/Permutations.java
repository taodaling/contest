package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class Permutations {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        if (n == 1) {
            out.println(1);
            return;
        }
        if (n <= 3) {
            out.println("NO SOLUTION");
            return;
        }
        for (int i = n; i >= 1; i--) {
            if (i % 2 == 1) {
                out.println(i);
            }
        }
        for (int i = n; i >= 1; i--) {
            if (i % 2 == 0) {
                out.println(i);
            }
        }
    }
}
