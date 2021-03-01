package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.DoubleEndPalindromeAutomaton;

public class Task {
    char[] s = new char[(int) 4e5];

    DoubleEndPalindromeAutomaton pa = new DoubleEndPalindromeAutomaton('a', 'z', (int) 4e5, (int) 4e5);

    public void addBack(int n) {
        for (int i = 0; i < n; i++) {
            pa.buildBack(s[i]);
        }
    }

    public void addFront(int n) {
        for (int i = 0; i < n; i++) {
            pa.buildFront(s[i]);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        addBack(n);
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                n = in.rs(s);
                addBack(n);
            } else if (t == 2) {
                n = in.rs(s);
                addFront(n);
            } else {
                out.println(pa.palindromeSubstringCnt());
            }
        }
    }
}
