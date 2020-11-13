package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.sql.SQLException;
import java.util.Arrays;

public class FBraveCHAIN {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[3 * n];
        in.populate(a);

        dp = new int[n + 1][n + 1];
        row = new int[n + 1];
        col = new int[n + 1];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, -inf);
        Arrays.fill(row, -inf);
        Arrays.fill(col, -inf);
        globalMax = -inf;
        update(a[0], a[1], 0);
        int[] rowMax = new int[n + 1];
        int[][] relatedRow = new int[3 * n][n + 1];
        int gm;
        debug.run(this::debug);
        int[] ee = new int[3 * n];
        for (int i = 2; i + 3 <= 3 * n; i += 3) {
            int l = i;
            int r = i + 3 - 1;
            boolean allSame = true;
            for (int j = l + 1; j <= r; j++) {
                if (a[j] != a[j - 1]) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) {
                fix++;
                continue;
            }
            gm = globalMax + fix;
            for (int j = 1; j <= n; j++) {
                rowMax[j] = row[j] + fix;
            }
            for (int j = l; j <= r; j++) {
                ee[j] = dp[a[j]][a[j]] + fix;
            }
            for (int j = l; j <= r; j++) {
                for (int k = 1; k <= n; k++) {
                    relatedRow[j][k] = dp[a[j]][k] + fix;
                }
            }

            //use two
            for (int j = l; j <= r; j++) {
                SequenceUtils.swap(a, j, l);
                update(a[l + 1], a[l + 2], ee[j] + 1);
                SequenceUtils.swap(a, j, l);
            }

            //use one
            for (int j = l; j <= r; j++) {
                SequenceUtils.swap(a, j, r);
                if (a[l] == a[l + 1]) {
                    for (int k = 1; k <= n; k++) {
                        update(a[r], k, relatedRow[j == l ? r : l][k] + 1);
                    }
                }
                SequenceUtils.swap(a, j, r);
            }

            //replace one
            for (int j = l; j <= r; j++) {
                int e = a[j];
                for (int k = 1; k <= n; k++) {
                    update(k, e, rowMax[k]);
                }
            }
            //replace two
            for (int j = l; j <= r; j++) {
                for (int k = j + 1; k <= r; k++) {
                    update(a[j], a[k], gm);
                }
            }

            debug.run(this::debug);
        }

        int last = a[a.length - 1];
        update(last, last, dp[last][last] + fix + 1);
        int ans = globalMax + fix;
        out.println(ans);
    }


    int[][] dp;
    int[] row;
    int[] col;
    int globalMax;
    int fix = 0;

    Debug debug = new Debug(false);

    public void debug() {
        StringBuilder builder = new StringBuilder("\n");
        int n = dp.length;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                builder.append(dp[i][j] + fix).append(' ');
            }
            builder.append('\n');
        }
        debug.debug("dp", builder);
    }

    public void update(int i, int j, int x) {
        x -= fix;
        if (dp[i][j] < x) {
            dp[i][j] = dp[j][i] = x;
            row[i] = Math.max(row[i], x);
            row[j] = Math.max(row[j], x);
            col[j] = Math.max(col[j], x);
            col[i] = Math.max(col[i], x);
            globalMax = Math.max(globalMax, x);
        }
    }
}
