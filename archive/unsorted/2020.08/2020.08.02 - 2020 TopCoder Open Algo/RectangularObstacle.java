package contest;

public class RectangularObstacle {
    public long sum(long n) {
        return n * (n + 1) / 2;
    }

    public long countReachable(int x1, int x2, int y1, int y2, int s) {
        if (s == 0) {
            return 1;
        }
        long ans = sum(s - 1) * 4 + (long) s * 4 + 1;
        for (int i = y1; i <= y2 && i <= s; i++) {
            int l = -(s - i);
            int r = -l;
            l = Math.max(l, x1);
            r = Math.min(r, x2);
            if (l <= r) {
                ans -= r - l + 1;
            }
        }

        for (int i = x1; i <= x2; i++) {
            int left = x1 - 1;
            int right = x2 + 1;
            long waste = Math.min(-left + (i - left),
                    right + (right - i));
            long originalHeight = s - Math.abs(i);
            long newHeight = Math.max(s - waste, y2);
            if (originalHeight > newHeight) {
                ans -= originalHeight - newHeight;
            }
        }

        return ans;
    }
}
