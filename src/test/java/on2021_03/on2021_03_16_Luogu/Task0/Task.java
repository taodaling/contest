package on2021_03.on2021_03_16_Luogu.Task0;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.KMPAutomaton;
import template.utils.SequenceUtils;

public class Task {

    char[] t = new char[(int) 3e6 + 1];
    int mask = 0;

    private int read(FastInput in) {
        int n = in.rs(t);
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < n; i++) {
            a.append((char) t[i]);
        }

        int z = mask;
        for (int i = 0; i < n; i++) {
            z = (z * 131 + i) % n;
            SequenceUtils.swap(t, i, z);
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < n; i++) {
            b.append((char) t[i]);
        }
        return n;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        KMPAutomaton kmp = new KMPAutomaton(s.length);
        for (char c : s) {
            kmp.build(c);
        }
        int n = read(in);
        for (int i = 0; i < n; i++) {
            out.append((char) t[i]);
        }
        out.println();
        kmp.beginMatch();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            kmp.match(t[i]);
            if (kmp.matchLast == n) {
                out.println(i);
                ans++;
            }
        }
        out.println(ans);
    }
}
