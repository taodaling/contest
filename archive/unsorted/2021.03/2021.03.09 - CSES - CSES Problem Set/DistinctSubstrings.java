package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.SubstringCompare;

public class DistinctSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        SubstringCompare sc = new SubstringCompare(new IntFunctionIntSequenceAdapter(i -> s[i], 0, n - 1));
        out.println(sc.distinctSubstring() - 1);
    }
}
