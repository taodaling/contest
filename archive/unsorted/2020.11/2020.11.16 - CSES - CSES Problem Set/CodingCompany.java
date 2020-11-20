package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CodingCompany {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] ws = new int[n];
        in.populate(ws);
        Arrays.sort(ws);
        int half = n / 2;

        int mod = (int) 1e9 + 7;
        long[][] prev = new long[half + 1][x + 1];
        long[][] next = new long[half + 1][x + 1];
        prev[0][0] = 1;
        int last = 0;
        for (int w : ws) {
            int d = w - last;
            last = w;
            for (int i = 0; i <= half; i++) {
                for (int j = 0; j <= x; j++) {
                    prev[i][j] %= mod;
                    next[i][j] = 0;
                }
            }
            for (int j = 0; j <= half; j++) {
                for (int k = 0; k <= x; k++) {
                    int to = k + j * d;
                    if (to > x || prev[j][k] == 0) {
                        continue;
                    }
                    next[j][to] += prev[j][k] * (j + 1) % mod;
                    //as open bracket
                    if (j + 1 <= half) {
                        next[j + 1][to] += prev[j][k];
                    }
                    //as close bracket
                    if (j - 1 >= 0) {
                        next[j - 1][to] += prev[j][k] * j % mod;
                    }
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = 0;
        for (int i = 0; i <= x; i++) {
            ans += prev[0][i];
        }
        ans %= mod;
        out.println(ans);
    }
}
