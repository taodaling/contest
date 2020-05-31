package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BPostdocs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            if (s[i] == '?') {
                s[i] = 'D';
            }
        }
        for (int i = 0; i < n; i++) {
            out.append(s[i]);
        }
    }
}
