package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class DAnOrdinaryGame {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[(int) 1e5];
        int n = in.readString(s, 0);
        if (s[0] == s[n - 1]) {
            out.println(n % 2 == 0 ? "First" : "Second");
        } else {
            out.println(n % 2 == 1 ? "First" : "Second");
        }
    }
}
