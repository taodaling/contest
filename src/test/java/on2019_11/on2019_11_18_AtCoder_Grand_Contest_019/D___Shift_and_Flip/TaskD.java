package on2019_11.on2019_11_18_AtCoder_Grand_Contest_019.D___Shift_and_Flip;



import java.util.Arrays;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskD {
    public char charAt(char[] c, int i) {
        return c[DigitUtils.mod(i, c.length)];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] a = in.readString().toCharArray();
        char[] b = in.readString().toCharArray();
        int n = a.length;

        if (SequenceUtils.indexOf(b, 0, n - 1, '1') == -1) {
            if (SequenceUtils.indexOf(a, 0, n - 1, '1') >= 0) {
                out.println(-1);
            } else {
                out.println(0);
            }
            return;
        }


        int[] lefts = new int[n];
        int[] rights = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                for (int j = 0;; j--) {
                    if (charAt(b, j) == '1') {
                        lefts[i] = j;
                        break;
                    }
                }
            } else {
                lefts[i] = lefts[i - 1];
                if (b[i] == '1') {
                    lefts[i] = i;
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                for (int j = n - 1;; j++) {
                    if (charAt(b, j) == '1') {
                        rights[i] = j;
                        break;
                    }
                }
            } else {
                rights[i] = rights[i + 1];
                if (b[i] == '1') {
                    rights[i] = i;
                }
            }
        }

        int[][] data = new int[n][2];
        int ans = (int) 1e9;

        // left rotate
        for (int i = 0; i <= n; i++) {
            int cost = i;
            for (int j = 0; j < n; j++) {
                if (a[j] != charAt(b, j - i)) {
                    cost++;
                    data[j][0] = Math.max(0, (j - i) - lefts[j]);
                    data[j][1] = rights[j] - j;
                } else {
                    data[j][0] = 0;
                    data[j][1] = 0;
                }
            }
            cost += cost(data) * 2;
            ans = Math.min(ans, cost);
        }

        // right rotate
        for (int i = 0; i <= n; i++) {
            int cost = i;
            for (int j = 0; j < n; j++) {
                if (a[j] != charAt(b, j + i)) {
                    cost++;
                    data[j][1] = Math.max(0, rights[j] - (j + i));
                    data[j][0] = j - lefts[j];
                } else {
                    data[j][0] = 0;
                    data[j][1] = 0;
                }

            }
            cost += cost(data) * 2;
            ans = Math.min(ans, cost);
        }

        out.println(ans);
    }

    public int cost(int[][] data) {
        Arrays.sort(data, (a, b) -> -Integer.compare(a[0], b[0]));
        int mx = 0;
        int ans = (int) 1e9;
        for (int l = 0, r; l < data.length; l = r + 1) {
            r = l;
            while (r + 1 < data.length && data[r + 1][0] == data[r][0]) {
                r++;
            }
            ans = Math.min(ans, mx + data[l][0]);
            for (int i = l; i <= r; i++) {
                mx = Math.max(mx, data[i][1]);
            }
        }
        return ans;
    }
}
