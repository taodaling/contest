package contest;

import java.io.PrintWriter;

import template.FastInput;

public class Task {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int k = in.readInt();
        for (int i = 1; i <= k; i++) {
            out.println(in.readInt() + i);
            out.flush();
        }
    }
}
