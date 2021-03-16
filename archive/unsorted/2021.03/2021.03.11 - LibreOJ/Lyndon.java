package contest;

import strings.LyndonDecomposition;
import template.io.FastInput;
import template.io.FastOutput;
import template.string.CharArrayCharSequenceAdapter;

import java.util.List;

public class Lyndon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e6];
        int n = in.rs(s);
        List<CharSequence> ans = LyndonDecomposition.decompose(new CharArrayCharSequenceAdapter(s, 0, n - 1));
        int offset = 0;
        for (CharSequence cs : ans) {
            offset += cs.length();
            out.append(offset).append(' ');
        }

    }
}
