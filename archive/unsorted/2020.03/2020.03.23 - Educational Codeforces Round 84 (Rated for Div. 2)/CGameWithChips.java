package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CGameWithChips {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        for (int i = 0; i < k; i++) {
            in.readInt();
            in.readInt();
        }

        StringBuilder ans = new StringBuilder(2 * n * m);
        for (int i = 0; i < n - 1; i++) {
            ans.append('U');
        }
        for (int i = 0; i < m - 1; i++) {
            ans.append('L');
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m - 1; j++) {
                ans.append(i % 2 == 0 ? 'R' : 'L');
            }
            if (i + 1 < n) {
                ans.append('D');
            }
        }

        out.println(ans.length());
        out.println(ans.toString());
    }
}
