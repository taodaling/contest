package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class CFairElevator {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[][] indexes = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                indexes[i][j] = in.readInt();
            }
        }

        int plus = 0;
        int minus = 1;
        int unknown = -1;

        int[] state = new int[n * 2 + 1];
        int[] len = new int[n * 2 + 1];
        int[] occur = new int[n * 2 + 1];
        int[] g = new int[n * 2 + 1];
        Arrays.fill(state, unknown);
        Arrays.fill(len, unknown);
        Arrays.fill(g, unknown);

        boolean valid = true;
        for (int i = 0; i < n; i++) {
            int l = indexes[i][0];
            int r = indexes[i][1];
            if (l != -1) {
                state[l] = plus;
                occur[l]++;
                g[l] = i;
            }
            if (r != -1) {
                state[r] = minus;
                occur[r]++;
                g[r] = i;
            }
            if (l != -1 && r != -1) {
                len[l] = len[r] = r - l + 1;
                if (l > r) {
                    valid = false;
                }
            }
        }

        for(int i = 1; i <= 2 * n; i++){
            if(occur[i] > 1){
                valid = false;
            }
        }

        if(!valid){
            out.println("No");
            return;
        }

        boolean[] dp = new boolean[n * 2 + 1];
        dp[0] = true;
        for (int i = 1; i <= n * 2; i++) {
            for (int j = 0; j < i && !dp[i]; j++) {
                if (!dp[j]) {
                    continue;
                }
                int l = j + 1;
                int r = i;
                if ((r - l + 1) % 2 != 0) {
                    continue;
                }

                boolean possible = true;
                int m = (l + r) / 2;
                int length = m + 1 - l + 1;
                for (int k = l; k <= m && possible; k++) {
                    if (state[k] == minus) {
                        possible = false;
                    }
                    if (len[k] != -1 && len[k] != length) {
                        possible = false;
                    }
                }
                for (int k = m + 1; k <= r && possible; k++) {
                    if (state[k] == plus) {
                        possible = false;
                    }
                    if (len[k] != -1 && len[k] != length) {
                        possible = false;
                    }
                }
                for(int k = l, t = m + 1; k <= m; k++, t++){
                    if(g[k] != -1 && g[t] != -1 && g[k] != g[t]){
                        possible = false;
                    }
                }

                if (possible) {
                    dp[i] = true;
                }
            }
        }

        out.println(dp[2 * n] ? "Yes" : "No");
    }
}
