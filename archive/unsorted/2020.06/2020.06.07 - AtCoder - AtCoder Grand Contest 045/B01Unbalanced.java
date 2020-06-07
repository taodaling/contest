package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class B01Unbalanced {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);

        int[][] l2r = new int[2][n];
        int[][] r2l = new int[2][n];
        int ans1 = solve(s, n, '0', l2r[0], r2l[0]);
        int ans2 = solve(s, n, '1', l2r[1], r2l[1]);
        int ans3 = 1;


        int[][] suffix = new int[2][2];
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] != '?') {
                if (ans1 == ans2 && l2r[0][i] == ans1 &&
                        suffix[1][(i & 1)] > 0) {
                    ans3 = Math.max(ans1 + 1, ans3);
                }
                if (l2r[0][i] == ans1 &&
                        suffix[0][(ans1 & 1) ^ (i & 1)] > 0) {
                    ans3 = Math.max(ans1 + 1, ans3);
                }
                if (l2r[1][i] == ans2 &&
                        suffix[1][(ans2 & 1) ^ (i & 1)] > 0) {
                    ans3 = Math.max(ans2 + 1, ans3);
                }
                if (ans1 == ans2 && l2r[1][i] == ans2 &&
                        suffix[0][(i & 1)] > 0) {
                    ans3 = Math.max(ans2 + 1, ans3);
                }

                if (r2l[0][i] == ans1) {
                    suffix[0][i & 1]++;
                }
                if (r2l[1][i] == ans2) {
                    suffix[1][i & 1]++;
                }

                continue;
            }
            int l = i;
            while (l > 0 && s[l - 1] == '?') {
                l--;
            }

            if (i + 1 < n && l > 0 && s[l - 1] == s[i + 1] && (i - l + 1) % 2 == 0) {
                ans3 = Math.max(ans3, 2);
            }
            i = l;
        }

        int ans = Math.max(ans1, ans2);
        ans = Math.max(ans, ans3);
        out.println(ans);
    }

    public int solve(char[] s, int n, int c, int[] l2r, int[] r2l) {
        int ans = 0;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == c) {
                cnt++;
            } else {
                cnt = Math.max(cnt - 1, 0);
            }
            l2r[i] = cnt;
            ans = Math.max(cnt, ans);
        }
        cnt = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] == c) {
                cnt++;
            } else {
                cnt = Math.max(cnt - 1, 0);
            }
            r2l[i] = cnt;
        }

        return ans;
    }
}
