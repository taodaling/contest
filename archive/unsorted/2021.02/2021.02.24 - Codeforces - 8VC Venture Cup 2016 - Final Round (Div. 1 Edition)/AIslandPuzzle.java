package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.KMPAutomaton;

public class AIslandPuzzle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n * 2];
        for (int i = 0; i < n; i++) {
            a[i] = a[i + n] = in.ri();
        }
        int[] b = in.ri(n);
        KMPAutomaton kmp = new KMPAutomaton(n);
        for (int i = 0; i < n; i++) {
            if (b[i] != 0) {
                kmp.build(b[i]);
            }
        }
        kmp.beginMatch();
        boolean ok = false;
        for (int i = 0; i < 2 * n; i++) {
            if (a[i] == 0) {
                continue;
            }
            kmp.match(a[i]);
            if (kmp.matchLast == n - 1) {
                ok = true;
            }
        }
        out.println(ok ? "YES" : "NO");
    }
}
