package contest;

import template.FastInput;
import template.FastOutput;
import template.IntList;

public class TaskD {

    int inf = (int) 1e9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);

        IntList list = new IntList(n);
        for (int i = 0; i < n; i++) {
            int j = i;
            while (j + 1 < n && s[j + 1] == s[i]) {
                j++;
            }
            list.add(j - i + 1);
            i = j;
        }
        if (s[n - 1] == '0') {
            list.pop();
        }

        int ans = 0;
        IntList cnts = new IntList(n);
        while (!list.isEmpty()) {
            cnts.clear();
            while (!list.isEmpty()) {
                cnts.add(list.pop());
                if (!list.isEmpty() && list.pop() == 1) {
                    continue;
                }
                break;
            }
            int m = cnts.size();
            // 0 for nothing, 1 for in, 2 for leave
            int[][] dp = new int[m + 1][3];
            dp[0][0] = 0;
            dp[0][1] = dp[0][2] = -inf;
            for (int i = 1; i <= m; i++) {
                int cnt = cnts.get(i - 1);

                for (int left = 0; left < 3; left++) {
                    for (int right = 0; right < 3; right++) {
                        if (left == 2 && right == 1) {
                            continue;
                        }
                        if (left == 1 && right == 2 && cnt < 2) {
                            continue;
                        }
                        if (left == 0) {
                            dp[i][right] = Math.max(dp[i][right], dp[i - 1][left] + (right == 1 ? cnt : 0));
                        }
                        if (left == 1) {
                            dp[i][right] = Math.max(dp[i][right], dp[i - 1][left] + (right == 1 ? cnt - 1 : 0));
                        }
                        if (left == 2) {
                            dp[i][right] = Math.max(dp[i][right], dp[i - 1][left] + (right == 2 ? cnt - 1 : cnt));
                        }
                    }
                }
            }

            ans += dp[m][0];
        }

        out.println(ans);
    }
}
