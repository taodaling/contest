package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class BCodeObfuscation {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int cur = 'a';
        for (char c : in.readString().toCharArray()) {
            if (c == cur) {
                cur++;
            } else if (c > cur) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
