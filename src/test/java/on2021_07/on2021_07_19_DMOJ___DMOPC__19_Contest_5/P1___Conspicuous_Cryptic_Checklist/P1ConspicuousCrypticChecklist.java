package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P1___Conspicuous_Cryptic_Checklist;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashSet;
import java.util.Set;

public class P1ConspicuousCrypticChecklist {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Set<String> set = new HashSet<>(n);
        for (int i = 0; i < n; i++) {
            set.add(in.rs());
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            int t = in.ri();
//            Set<String> unique = new HashSet<>(t);
            boolean ok = true;
            for (int j = 0; j < t; j++) {
                String s = in.rs();
//                unique.add(s);
                ok = ok && set.contains(s);
            }
            if (ok) {
                ans++;
            }
        }
        out.println(ans);
    }
}
