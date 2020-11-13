package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.KMPAutomaton;

public class StringMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] a = new char[(int) 1e6];
        char[] b = new char[(int) 1e6];
        int n = in.readString(a, 0);
        int m = in.readString(b, 0);
        KMPAutomaton kmp = new KMPAutomaton(m);
        for (int i = 0; i < m; i++) {
            kmp.build(b[i]);
        }
        int ans = 0;
        kmp.beginMatch();
        for (int i = 0; i < n; i++) {
            kmp.match(a[i]);
            if (kmp.isMatch()) {
                ans++;
            }
        }
        out.println(ans);
    }
}
