package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ALettersCyclicShift {
    public char next(char c) {
        if (c == 'a') {
            return 'z';
        }
        return (char) (c - 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int len = in.readString(s, 0);
        int state = 0;
        for (int i = 0; i < len; i++) {
            if (state <= 1 && next(s[i]) < s[i]) {
                state = 1;
                s[i] = next(s[i]);
            } else if (state == 1 && next(s[i]) > s[i]) {
                state = 2;
            }
        }

        if (state == 0) {
            s[len - 1] = next(s[len - 1]);
        }
        for (int i = 0; i < len; i++) {
            out.append(s[i]);
        }
    }
}
