package on2019_12.on2019_12_08_AtCoder_Beginner_Contest_147.E___Balanced_Path;





import template.io.FastInput;
import template.io.FastOutput;
import template.utils.ArrayIndex;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int[][] a = new int[h][w];
        int[][] b = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                a[i][j] = in.readInt();
            }
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                b[i][j] = in.readInt();
            }
        }
        ArrayIndex ai = new ArrayIndex(h, w, 80 * 160);
        boolean[] dp = new boolean[ai.totalSize()];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (i == 0 && j == 0) {
                    dp[ai.indexOf(0, 0, Math.abs(b[0][0] -a[0][0]))] = true;
                }
                for (int k = 0; k < 80 * 160; k++) {
                    if (!dp[ai.indexOf(i, j, k)]) {
                        continue;
                    }
                    if (i + 1 < h) {
                        dp[ai.indexOf(i + 1, j, Math.abs(k + b[i + 1][j] - a[i + 1][j]))] = true;
                        dp[ai.indexOf(i + 1, j, Math.abs(k - b[i + 1][j] + a[i + 1][j]))] = true;
                    }
                    if (j + 1 < w) {
                        dp[ai.indexOf(i, j + 1, Math.abs(k + b[i][j + 1] - a[i][j + 1]))] = true;
                        dp[ai.indexOf(i, j + 1, Math.abs(k - b[i][j + 1] + a[i][j + 1]))] = true;
                    }
                }
            }
        }

        for (int i = 0; i < 80 * 160; i++) {
            if (dp[ai.indexOf(h - 1, w - 1, i)]) {
                out.println(i);
                return;
            }
        }
    }
}
