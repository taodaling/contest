package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[1000000];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }

        BitOperator bo = new BitOperator();
        int[] len = new int[1 << 20];
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = i; j < i + 20 && j < n; j++) {
                if (bo.bitAt(mask, s[j]) == 1) {
                    break;
                }
                mask = bo.setBit(mask, s[j], true);
                len[mask] = j - i + 1;
            }
        }

        for (int i = 1; i < (1 << 20); i++) {
            if (Integer.lowestOneBit(i) == i) {
                continue;
            }
            for (int j = 0; j < 20; j++) {
                if (bo.bitAt(i, j) == 0) {
                    continue;
                }
                len[i] = Math.max(len[i], len[bo.setBit(i, j, false)]);
            }
        }

        int ans = 0;
        int mask = (1 << 20) - 1;
        for (int i = 0; i < (1 << 20); i++) {
            ans = Math.max(ans, len[i] + len[mask - i]);
        }
        out.println(ans);
    }
}
