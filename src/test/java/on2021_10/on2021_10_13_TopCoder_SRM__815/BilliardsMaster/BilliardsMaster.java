package on2021_10.on2021_10_13_TopCoder_SRM__815.BilliardsMaster;



import template.math.DigitUtils;

import java.util.Arrays;

public class BilliardsMaster {
    public int[] play(int tx, int ty, int sx, int sy, int fx, int fy, int b) {
        int[] ans = new int[4];
        int[] js = new int[2];
        long best = (long) 1e18;
        for (int i = -b; i <= b; i++) {
            js[0] = b - Math.abs(i);
            js[1] = -js[0];
            for (int j : js) {
                long tox = i % 2 == 0 ? fx : mirror(fx, tx);
                long toy = j % 2 == 0 ? fy : mirror(fy, ty);
                tox += (long) i * tx;
                toy += (long) j * ty;
                long d2 = dist2(sx, sy, tox, toy);
                if (d2 < best) {
                    best = d2;
                    Arrays.fill(ans, 0);
                }
                if (d2 == best) {
                    int left, right, up, down;
                    if (i < 0) {
                        left = DigitUtils.ceilDiv(-i, 2);
                    } else {
                        left = DigitUtils.floorDiv(i, 2);
                    }

                    if (j < 0) {
                        down = DigitUtils.ceilDiv(-j, 2);
                    } else {
                        down = DigitUtils.floorDiv(j, 2);
                    }
                    right = Math.abs(i) - left;
                    up = Math.abs(j) - down;

                    ans[0] = Math.max(ans[0], left);
                    ans[1] = Math.max(ans[1], right);
                    ans[2] = Math.max(ans[2], up);
                    ans[3] = Math.max(ans[3], down);
                }
            }
        }

        Arrays.sort(ans);
        return ans;
    }

    public long dist2(long x1, long y1, long x2, long y2) {
        long dx = x1 - x2;
        long dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public int mirror(int x, int b) {
        return b - x;
    }
}
