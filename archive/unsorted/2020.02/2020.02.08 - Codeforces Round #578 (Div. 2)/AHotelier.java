package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AHotelier {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] status = new int[10];
        for (int i = 0; i < n; i++) {
            char c = in.readChar();
            if (c == 'L') {
                for (int j = 0; j < 10; j++) {
                    if (status[j] == 0) {
                        status[j] = 1;
                        break;
                    }
                }
            } else if (c == 'R') {
                for (int j = 10 - 1; j >= 0; j--) {
                    if (status[j] == 0) {
                        status[j] = 1;
                        break;
                    }
                }
            } else {
                status[c - '0'] = 0;
            }
        }

        for (int i = 0; i < 10; i++) {
            out.append(status[i]);
        }
    }
}
