package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.Comparator;

public class CEdoAndMagnets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();

        long[][] pts = new long[n][2];
        for (int i = 0; i < n; i++) {
            long x1 = in.ri();
            long y1 = in.ri();
            long x2 = in.ri();
            long y2 = in.ri();
            pts[i][0] = x1 + x2;
            pts[i][1] = y1 + y2;
        }

        long[][] sortedByX = pts.clone();
        long[][] sortedByY = pts.clone();
        long[][] filtered = pts.clone();
        Arrays.sort(sortedByX, Comparator.comparingLong(x -> x[0]));
        Arrays.sort(sortedByY, Comparator.comparingLong(x -> x[1]));
        long area = (long) 4e18;
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= k; j++) {
                if (i + j > k) {
                    continue;
                }
                long bot = sortedByY[i][1];
                long top = sortedByY[n - 1 - j][1];
                int cnt = 0;
                int wpos = 0;
                for (int t = 0; t < n; t++) {
                    if (sortedByX[t][1] > top || sortedByX[t][1] < bot) {
                        cnt++;
                    } else {
                        filtered[wpos++] = sortedByX[t];
                    }
                }
                if (cnt > k) {
                    continue;
                }
                int remain = k - cnt;
                for (int t = 0; t <= remain; t++) {
                    int l = t;
                    int r = wpos - 1 - (remain - t);
                    long height = DigitUtils.ceilDiv(top - bot, 2);
                    long widht = DigitUtils.ceilDiv(filtered[r][0] - filtered[l][0], 2);
                    height = Math.max(height, 1);
                    widht = Math.max(widht, 1);
                    area = Math.min(area, height * widht);
                }
            }
        }

        out.println(area);
    }
}
