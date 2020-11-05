package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.SparseSuffixAutomaton;
import template.string.SuffixAutomaton;

public class SDOI2016 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        SparseSuffixAutomaton ssa = new SparseSuffixAutomaton();
        ssa.enableDistinctSubstr();
        for (int i = 0; i < n; i++) {
            ssa.build(in.readInt());
            out.println(ssa.distinctSubstr);
        }
    }
}
