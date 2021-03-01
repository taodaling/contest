package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ATreasure {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        int l = 0;
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == '(') {
                l++;
                r++;
            } else if (s[i] == ')') {
                l--;
                r--;
            } else {
                l = 0;
                r--;
            }
            l = Math.max(0, l);
            if (r < 0) {
                out.println(-1);
                return;
            }
        }
        if (l > 0) {
            out.println(-1);
            return;
        }
        int leftTotal = 0;
        int rightTotal = 0;
        int total = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == '#') {
                total++;
            } else if (s[i] == '(') {
                leftTotal++;
            } else {
                rightTotal++;
            }
        }
        for (int i = 0; i < total - 1; i++) {
            out.println(1);
        }
        out.println(leftTotal - rightTotal - (total - 1));
    }
}
