package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixAutomaton;

public class DistinctSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int)1e5];
        int n = in.rs(s);
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        sa.enableDistinctSubstr();
        for(int i = 0; i < n; i++){
            sa.build(s[i]);
        }
        out.println(sa.realTimeDistinctSubstr);
    }
}
