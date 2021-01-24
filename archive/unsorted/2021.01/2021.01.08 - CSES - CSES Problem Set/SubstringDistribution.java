package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixAutomaton;

public class SubstringDistribution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        long k = in.rl();
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z', n);
        for (int i = 0; i < n; i++) {
            sa.build(s[i]);
        }
        long[] lens = new long[n + 2];
        for (SuffixAutomaton.SANode node : sa.all) {
            int l = node.minLength();
            int r = node.maxlen;
            lens[l] += 1;
            lens[r + 1] -= 1;
        }
        for(int i = 1; i < n + 2; i++){
            lens[i] += lens[i - 1];
        }
        for(int i = 1; i <= n; i++){
            out.append(lens[i]).append(' ');
        }
    }
}
