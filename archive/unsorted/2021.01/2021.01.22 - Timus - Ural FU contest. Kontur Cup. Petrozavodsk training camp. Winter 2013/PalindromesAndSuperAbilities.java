package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.PalindromeAutomaton;

public class PalindromesAndSuperAbilities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        PalindromeAutomaton pa = new PalindromeAutomaton('a', 'z', n);
        for(int i = 0; i < n; i++){
            pa.build(s[i]);
            out.println(pa.distinctPalindromeSubstring());
        }
    }
}
