package contest;

import com.topcoder.shared.problem.IntegralValue;
import template.datastructure.MultiWayDeque;
import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EscapingCourses {
    long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        boolean[][] blocked = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                blocked[i][j] = in.readChar() == '#';
            }
        }

        int restrict = in.readInt();

        List<IntervalXY> constrains = new ArrayList<>(restrict);
        for (int i = 0; i < restrict; i++) {
            int x = in.readInt() - 1;
            int y = in.readInt() - 1;
            constrains.add(new IntervalXY(in.readInt(), in.readInt(), x, y));
        }
        constrains.sort((a, b) -> Long.compare(a.l, b.l));
        long[][] last = new long[n][m];
        MultiWayDeque<IntervalXY> dqs = new MultiWayDeque<>(n * m, n * m + restrict);
        for (IntervalXY interval : constrains) {
            IntervalXY spare = new IntervalXY(last[interval.x][interval.y],
                    interval.l - 1, interval.x, interval.y);
            last[interval.x][interval.y] = interval.r + 1;
            dqs.addLast(interval.x * m + interval.y, spare);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dqs.addLast(i * m + j, new IntervalXY(last[i][j], inf, i, j));
            }
        }

        PriorityQueue<IntervalXY> pq = new PriorityQueue<>(n * m + restrict, (a, b) -> Long.compare(a.l, b.l));
        pq.add(dqs.removeFirst(0));
        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        while (!pq.isEmpty()) {
            IntervalXY head = pq.remove();
            if (head.l > head.r) {
                continue;
            }
            if (head.x == n - 1 && head.y == m - 1) {
                out.println(head.l);
                return;
            }
            for (int[] dir : dirs) {
                int x = dir[0] + head.x;
                int y = dir[1] + head.y;
                if (x < 0 || x >= n || y < 0 || y >= m || blocked[x][y]) {
                    continue;
                }
                long l = head.l + 1;
                long r = head.r + 1;

                int id = x * m + y;
                while (!dqs.isEmpty(id)) {
                    IntervalXY first = dqs.peekFirst(id);
                    if (first.l > r) {
                        break;
                    }
                    dqs.removeFirst(id);
                    if (first.r < l) {
                        continue;
                    }
                    first.l = Math.max(first.l, l);
                    pq.add(first);

                }

            }
        }

        out.println(-1);
    }
}

class IntervalXY extends Interval {
    int x;
    int y;

    public IntervalXY(long l, long r, int x, int y) {
        super(l, r);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("(%d, %d)", x, y);
    }
}

class Interval {
    long l;
    long r;

    static Comparator<Interval> sortByL = (a, b) -> -Long.compare(a.l, b.l);

    public Interval(long l, long r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", l, r);
    }
}