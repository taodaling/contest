package contest;

public class SecondDiameters {
    public Dist[] first2(int[] X, int[] Y, int skip) {
        Dist largest = new Dist();
        Dist second = null;
        int n = X.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == skip || j == skip) {
                    continue;
                }
                Dist dist = new Dist();
                dist.i = i;
                dist.j = j;
                dist.dist2 = dist2(X[i], Y[i], X[j], Y[j]);
                if (second == null) {
                    if (largest.dist2 != dist.dist2) {
                        second = dist;
                    }
                } else {
                    if (dist.dist2 != largest.dist2 && dist.dist2 > second.dist2) {
                        second = dist;
                    }
                }
                if (second != null && second.dist2 > largest.dist2) {
                    Dist tmp = largest;
                    largest = second;
                    second = tmp;
                }
            }
        }
        return new Dist[]{largest, second};
    }

    public long getSecondDiameters(int[] X, int[] Y) {
        Dist[] normal = first2(X, Y, -1);
        int n = X.length;
        long[] ans = new long[n];
        for (int t = 0; t < n; t++) {
            boolean find = false;
            if (normal[0] != null && (normal[0].i == t || normal[0].j == t)) {
                find = true;
            }
            if (normal[1] != null && (normal[1].i == t || normal[1].j == t)) {
                find = true;
            }
            if (!find) {
                ans[t] = normal[1].dist2;
                continue;
            }
            ans[t] = first2(X, Y, t)[1].dist2;
        }
        long sum = 0;
        for (long x : ans) {
            sum += x;
        }
        return sum;
    }

    public long dist2(long x1, long y1, long x2, long y2) {
        long dx = x1 - x2;
        long dy = y1 - y2;
        return dx * dx + dy * dy;
    }
}


class Dist {
    int i;
    int j;
    long dist2;
}