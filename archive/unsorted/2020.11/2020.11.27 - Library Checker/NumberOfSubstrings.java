package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixAutomaton;

public class NumberOfSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int)5e5];
        int n = in.readString(s, 0);
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        sa.enableDistinctSubstr();
        for(int i = 0; i < n; i++){
            sa.build(s[i]);
        }
        out.println(sa.realTimeDistinctSubstr);
    }
}
