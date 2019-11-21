package contest;

import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.readInt();
            }
        }

        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        int ans = pow.pow(2, n);
        ans = mod.subtract(ans, 1);
        ans = mod.subtract(ans, n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                boolean valid = true;
                for (int k = 0; k < j; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    if (cross(pts[j][0] - pts[i][0], pts[j][1] - pts[i][1], pts[k][0] - pts[i][0],
                                    pts[k][1] - pts[i][1]) == 0) {
                        valid = false;
                    }
                }
                if (!valid) {
                    continue;
                }

                int cnt = 2;
                for (int k = j + 1; k < n; k++) {
                    if (cross(pts[j][0] - pts[i][0], pts[j][1] - pts[i][1], pts[k][0] - pts[i][0],
                                    pts[k][1] - pts[i][1]) == 0) {
                        cnt++;
                    }
                }

                int local = pow.pow(2, cnt);
                local = mod.subtract(local, 1);
                local = mod.subtract(local, cnt);
                ans = mod.subtract(ans, local);
            }
        }

        out.println(ans);
    }

    int cross(int x1, int y1, int x2, int y2) {
        return x1 * y2 - y1 * x2;
    }
}
