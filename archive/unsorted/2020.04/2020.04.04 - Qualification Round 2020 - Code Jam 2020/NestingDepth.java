package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.stream.Stream;

public class NestingDepth {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        char[] s = in.readString().toCharArray();
        add(out, '(', s[0] - '0');
        out.append(s[0]);
        for (int i = 1; i < s.length; i++) {
            if (s[i] >= s[i - 1]) {
                add(out, '(', s[i] - s[i - 1]);
            } else {
                add(out, ')', s[i - 1] - s[i]);
            }
            out.append(s[i]);
        }
        add(out, ')', s[s.length - 1] - '0');
        out.println();
    }

    public void add(FastOutput out, char c, int n) {
        for (int i = 0; i < n; i++) {
            out.append(c);
        }
    }
}
