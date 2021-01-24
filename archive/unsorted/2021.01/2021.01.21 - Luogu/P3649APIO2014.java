package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.PalindromeAutomaton;

import java.util.function.Consumer;

public class P3649APIO2014 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 3e5];
        int n = in.rs(s);
        PalindromeAutomaton pa = new PalindromeAutomaton('a', 'z', n);
        for (int i = 0; i < n; i++) {
            pa.build(s[i]);
        }
        pa.endBuild();
        Visitor ans = new Visitor();
        pa.visit(ans);
        out.println(ans.getMax());
    }


}

class Visitor implements Consumer<PalindromeAutomaton.Node> {
    long max = 0;

    @Override
    public void accept(PalindromeAutomaton.Node node) {
        max = Math.max(max, (long)node.len * node.occurTime);
    }

    public long getMax() {
        return max;
    }
}