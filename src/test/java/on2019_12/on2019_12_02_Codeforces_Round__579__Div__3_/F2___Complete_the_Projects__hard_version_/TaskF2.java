package on2019_12.on2019_12_02_Codeforces_Round__579__Div__3_.F2___Complete_the_Projects__hard_version_;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TaskF2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int r = in.readInt();
        List<Project> pos = new ArrayList<>(n);
        List<Project> neg = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Project p = new Project();
            p.a = in.readInt();
            p.b = in.readInt();
            if (p.b < 0) {
                neg.add(p);
            } else {
                pos.add(p);
            }
        }

        pos.sort((a, b) -> a.a - b.a);
        int ans = 0;
        for (Project p : pos) {
            if (p.a > r) {
                break;
            }
            ans++;
            r += p.b;
        }

        neg.sort((a, b) -> -(a.a + a.b - b.a - b.b));
        int m = neg
                .size();
        int[][] dp = new int[m + 1][m + 1];
        SequenceUtils.deepFill(dp, -1);
        dp[0][0] = r;
        for (int i = 1; i <= m; i++) {
            Project p = neg.get(i - 1);
            for (int j = 0; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0 && dp[i - 1][j - 1] >= p.a) {
                    dp[i][j] = Math.max(dp[i][j],
                            dp[i - 1][j - 1] + p.b);
                }
            }
        }

        int max = 0;
        for (int i = 0; i <= m; i++) {
            if (dp[m][i] >= 0) {
                max = i;
            }
        }

        out.println(ans + max);
    }
}

class Project {
    int a;
    int b;

    @Override
    public String toString() {
        return String.format("(%d,%d)", a, b);
    }
}