package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixBalancedTree;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int)1e6];
        int n = in.rs(s);
        SuffixBalancedTree sbt = new SuffixBalancedTree();
        for (int i = n - 1; i >= 0; i--) {
            sbt.addPrefix(s[i]);
        }
        int[] sa = sbt.sa();
        for (int i = 0; i < n; i++) {
            out.append(n - sa[i]).append(' ');
        }
    }
}
