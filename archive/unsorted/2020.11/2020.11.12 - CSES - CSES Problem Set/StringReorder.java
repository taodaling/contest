package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class StringReorder {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        int charset = 'Z' - 'A' + 1;
        int[] cnts = new int[charset];
        for (int i = 0; i < n; i++) {
            cnts[s[i] - 'A']++;
        }
        int threshold = DigitUtils.ceilDiv(n, 2);
        for (int i = 0; i < charset; i++) {
            if (cnts[i] > threshold) {
                out.println(-1);
                return;
            }
        }
        int remain = n;
        int pre = -1;
        while (remain > 0) {
            int max = 0;
            remain--;
            threshold = (remain + 1) >> 1;
            for (int x : cnts) {
                max = Math.max(max, x);
            }
            boolean added = false;
            for (int j = 0; j < charset; j++) {
                if (pre == j || cnts[j] == 0 || cnts[j] != max && max > threshold) {
                    continue;
                }
                added = true;
                cnts[j]--;
                pre = j;
                out.append((char) ('A' + j));
                break;
            }
            assert added;
        }
    }
}
