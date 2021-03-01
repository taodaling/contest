package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.PalindromeAutomaton;

public class P5496PAM {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.rs(s);
        PalindromeAutomaton pa = new PalindromeAutomaton('a', 'z', n);
        int last = 0;
        for (int i = 0; i < n; i++) {
            int next = (s[i] - 97 + last) % 26 + 97;
            pa.build((char)next);
            out.append(last = pa.buildLast.depth).append(' ');
        }
    }
}
