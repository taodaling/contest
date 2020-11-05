package contest;

import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class FSilverWoods {
    public double dist(int a, int b, int x, int y) {
        double dx = a - x;
        double dy = b - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int bound = (int) 1e9;
        int n = in.readInt();
        double r = 100;
        int[][] pts = new int[n][2];
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pts[i][0] = x;
            pts[i][1] = y;
            r = Math.min(r, dist(x, y, -bound, 0));
            r = Math.min(r, dist(x, y, bound, 0));
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                dist[i][j] = dist[j][i] = dist(pts[i][0], pts[i][1],
                        pts[j][0], pts[j][1]);
            }
        }

        boolean[] access = new boolean[n];
        Deque<Integer> dq = new ArrayDeque<>();
        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-10, 1e-10) {
            @Override
            public boolean check(double mid) {
                Arrays.fill(access, false);
                dq.clear();
                for (int i = 0; i < n; i++) {
                    if (pts[i][1] + mid >= 100 - mid) {
                        access[i] = true;
                        dq.addLast(i);
                    }
                }
                while (!dq.isEmpty()) {
                    int head = dq.removeFirst();
                    for (int i = 0; i < n; i++) {
                        if (access[i] || dist[i][head] > 2 * mid) {
                            continue;
                        }
                        access[i] = true;
                        dq.addLast(i);
                    }
                }
                for (int i = 0; i < n; i++) {
                    if (access[i] && pts[i][1] - mid <= -100 + mid) {
                        return true;
                    }
                }
                return false;
            }
        };

        double ans = dbs.binarySearch(0, r);
        out.println(ans);
    }
}
