package on2020_03.on2020_03_05_Dwango_Programming_Contest_V.D___Square_Rotation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.RandomWrapper;
import template.utils.Debug;

public class DSquareRotation {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int[][] mat = new int[d][d];
        int[][] size = new int[d][d];
        for (int i = 0; i < n; i++) {
            int x = DigitUtils.mod(in.readInt(), d);
            int y = DigitUtils.mod(in.readInt(), d);
            mat[x][y]++;
        }

        RandomWrapper rw = new RandomWrapper();
        int[][] h = new int[d][d];
        int[][] w = new int[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                size[i][j] = 0;
                while (size[i][j] * size[i][j] < mat[i][j]) {
                    size[i][j]++;
                }

                h[i][j] = size[i][j];
                w[i][j] = DigitUtils.ceilDiv(mat[i][j], h[i][j]);
                h[i][j] = 1 + (h[i][j] - 1) * d;
                w[i][j] = 1 + (w[i][j] - 1) * d;
            }
        }

        debug.debug("mat", mat);
        debug.debug("size", size);

        long end = System.currentTimeMillis() + 2000;
        long ans = (long) 1e18;
        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    if (mat[i][j] == 0) {
                        continue;
                    }
                    if (rw.nextInt(0, 1) == 1) {
                        int tmp = h[i][j];
                        h[i][j] = w[i][j];
                        w[i][j] = tmp;
                    }
                }
            }

            int top = 0;
            int right = 0;
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    if (size[i][j] == 0) {
                        continue;
                    }
                    top = Math.max(top, i + h[i][j] - 1);
                    right = Math.max(right, j + w[i][j] - 1);
                }
            }
            int height = top + 1;
            int width = right + 1;
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    if (mat[i][j] == 0) {
                        continue;
                    }
                    top = Math.max(top, i + h[i][j] - 1 + d);
                }
                height = Math.min(height, top - i);
            }
            for (int j = 0; j < d; j++) {
                for (int i = 0; i < d; i++) {
                    if (mat[i][j] == 0) {
                        continue;
                    }
                    right = Math.max(right, j + w[i][j] - 1 + d);
                }
                width = Math.min(width, right - j);
            }

            ans = Math.min(Math.min(height, width) - 1, ans);
        }

        out.println(ans);
    }

}
