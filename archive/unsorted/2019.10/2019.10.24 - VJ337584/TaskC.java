package contest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt() - 1;
        int b = in.readInt() - 1;

        long[][] pts = new long[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.readInt();
            }
            long x = pts[i][0] + pts[i][1];
            long y = pts[i][0] - pts[i][1];
            pts[i][0] = x;
            pts[i][1] = y;
        }

        TreeMap<Long, TreeMap<Long, long[]>> x2y = new TreeMap<>();
        TreeMap<Long, TreeMap<Long, long[]>> y2x = new TreeMap<>();

        List<long[]> visited = new ArrayList<>(n);
        Deque<long[]> pending = new ArrayDeque<>(n);
        pending.addLast(pts[a]);
        pending.addLast(pts[b]);
        for (int i = 0; i < n; i++) {
            if (i == a || i == b) {
                continue;
            }
            putInto(pts[i][0], pts[i][1], pts[i], x2y);
            putInto(pts[i][1], pts[i][0], pts[i], y2x);
        }

        long dist = chebyshev(pts[a], pts[b]);
        while (!pending.isEmpty()) {
            long[] pt = pending.removeFirst();
            visited.add(pt);

            // consider l side
            remove(x2y, y2x, pt[0] - dist, pt[1] - dist, pt[1] + dist, pending);
            remove(x2y, y2x, pt[0] + dist, pt[1] - dist, pt[1] + dist, pending);
            remove(y2x, x2y, pt[1] - dist, pt[0] - dist, pt[0] + dist, pending);
            remove(y2x, x2y, pt[1] + dist, pt[0] - dist, pt[0] + dist, pending);
        }

        TreeMap<Long, TreeMap<Long, Integer>> y2xRank = new TreeMap<>();
        TreeMap<Long, TreeMap<Long, Integer>> x2yRank = new TreeMap<>();
        visited.sort((a1, a2) -> Long.compare(a1[0], a2[0]));
        for (long[] pt : visited) {
            TreeMap<Long, Integer> inner = y2xRank.get(pt[1]);
            if (inner == null) {
                inner = new TreeMap<>();
                y2xRank.put(pt[1], inner);
            }
            inner.put(pt[0], inner.size() + 1);
        }
        visited.sort((a1, a2) -> Long.compare(a1[1], a2[1]));
        for (long[] pt : visited) {
            TreeMap<Long, Integer> inner = x2yRank.get(pt[0]);
            if (inner == null) {
                inner = new TreeMap<>();
                x2yRank.put(pt[0], inner);
            }
            inner.put(pt[1], inner.size() + 1);
        }

        long ans = 0;
        for (long[] pt : visited) {
            long local = countOf(x2yRank, pt[0] - dist, pt[1] - dist, pt[1] + dist)
                            + countOf(x2yRank, pt[0] + dist, pt[1] - dist, pt[1] + dist)
                            + countOf(y2xRank, pt[1] - dist, pt[0] - dist + 1, pt[0] + dist - 1)
                            + countOf(y2xRank, pt[1] + dist, pt[0] - dist + 1, pt[0] + dist - 1);
            ans += local;
        }

        out.println(ans / 2);
    }

    public int countOf(TreeMap<Long, TreeMap<Long, Integer>> map, Long k, Long l, Long r) {
        if (l > r) {
            return 0;
        }
        TreeMap<Long, Integer> inner = map.get(k);
        if (inner == null) {
            return 0;
        }
        Map.Entry<Long, Integer> floor = inner.ceilingEntry(l);
        Map.Entry<Long, Integer> ceil = inner.floorEntry(r);
        if (floor == null || ceil == null) {
            return 0;
        }
        return Math.max(0, ceil.getValue() - floor.getValue() + 1);
    }

    public void remove(TreeMap<Long, TreeMap<Long, long[]>> map, TreeMap<Long, TreeMap<Long, long[]>> opp, Long k1,
                    Long l, Long r, Deque<long[]> pending) {
        TreeMap<Long, long[]> inner = map.get(k1);
        if (inner == null) {
            return;
        }
        while (true) {
            Long y = inner.ceilingKey(l);
            if (y == null || y > r) {
                break;
            }
            pending.addLast(inner.remove(y));
            opp.get(y).remove(k1);
        }
    }


    public void putInto(Long k1, Long k2, long[] pt, TreeMap<Long, TreeMap<Long, long[]>> map) {
        TreeMap<Long, long[]> inner = map.get(k1);
        if (inner == null) {
            inner = new TreeMap<>();
            map.put(k1, inner);
        }
        inner.put(k2, pt);
    }

    public long chebyshev(long[] a, long[] b) {
        return Math.max(Math.abs(a[0] - b[0]), Math.abs(a[1] - b[1]));
    }
}

