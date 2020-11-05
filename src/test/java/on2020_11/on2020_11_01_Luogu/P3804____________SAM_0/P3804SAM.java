package on2020_11.on2020_11_01_Luogu.P3804____________SAM_0;





import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixAutomaton;

import java.util.ArrayList;
import java.util.List;

public class P3804SAM {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        sa.all = new ArrayList<>(2 * n);
        for (int i = 0; i < n; i++) {
            sa.build(s[i]);
        }
        sa.calcRight(i -> s[i], n);
        long ans = 0;
        for(SuffixAutomaton.SANode node : sa.all){
            if(node.right > 1){
                ans = Math.max(node.right * (long)node.maxlen, ans);
            }
        }
        out.println(ans);
    }
}
