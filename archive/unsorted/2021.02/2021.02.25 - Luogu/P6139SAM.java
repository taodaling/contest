package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixAutomaton;

public class P6139SAM {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[(int)1e6];
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z', (int)1e6);
        sa.enableDistinctSubstr();
        for(int i = 0; i < n; i++){
            int m = in.rs(s);
            sa.buildLast = sa.root;
            for(int j = 0; j < m; j++){
                sa.build(s[j]);
            }
        }
        out.println(sa.realTimeDistinctSubstr);
    }
}
