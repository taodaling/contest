package on2019_10.on2019_10_11_Atcoder_ARC061.TaskA;



import java.io.PrintWriter;

import template.FastInput;

public class TaskA {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = in.readString().toCharArray();
        int n = s.length;
        long[][] interval = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    interval[i][j] = s[i] - '0';
                } else {
                    interval[i][j] = interval[i][j - 1] * 10 + s[j] - '0';
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                ans += interval[i][j] * wayOf(i) * wayOf(n - 1 - j);
            }
        }

        out.println(ans);
    }

    public int wayOf(int n) {
        if (n == 0) {
            return 1;
        }
        return 1 << (n - 1);
    }
}
