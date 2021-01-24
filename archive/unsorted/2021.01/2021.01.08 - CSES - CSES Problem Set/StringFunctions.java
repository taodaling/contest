package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.KMPAutomaton;
import template.string.ZAlgorithm;

public class StringFunctions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.rs(s, 0);
        KMPAutomaton kmp = new KMPAutomaton(n);
        for (int i = 0; i < n; i++) {
            kmp.build(s[i]);
        }
        int[] z = ZAlgorithm.generate(i -> s[i], n);
        out.append(0).append(' ');
        for (int i = 1; i < n; i++) {
            out.append(z[i]).append(' ');
        }
        out.println();
        for (int i = 0; i < n; i++) {
            out.append(kmp.maxBorder(i)).append(' ');
        }
    }
}
