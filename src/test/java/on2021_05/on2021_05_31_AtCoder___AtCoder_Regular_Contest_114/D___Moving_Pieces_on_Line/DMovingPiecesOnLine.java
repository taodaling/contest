package on2021_05.on2021_05_31_AtCoder___AtCoder_Regular_Contest_114.D___Moving_Pieces_on_Line;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class DMovingPiecesOnLine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        List<Point> ptList = new ArrayList<>();
        Map<Integer, Integer> cntMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            cntMap.put(a, cntMap.getOrDefault(a, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : cntMap.entrySet()) {
            int key = entry.getKey();
            int val = entry.getValue();
            while (val >= 2) {
                val -= 2;
                ptList.add(new Point(key, false));
            }
        }
        for (int i = 0; i < k; i++) {
            int x = in.ri();
            ptList.add(new Point(x, true));
            if (cntMap.getOrDefault(x, 0) % 2 == 1) {
                ptList.add(new Point(x, false));
            }
        }

        ptList.sort(Comparator.comparingLong(x -> x.x));
        Point[] pts = ptList.toArray(new Point[0]);
        int m = Math.max(n, k);
        int zero = m;
        long[] prev = new long[zero + m + 1];
        long[] next = new long[zero + m + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(prev, inf);
        prev[zero] = 0;
        long lastDist = (long) -1e9;
        for (Point pt : pts) {
            SequenceUtils.deepFill(next, inf);
            for (int j = -m; j <= m; j++) {
                prev[j + zero] += (pt.x - lastDist) * Math.abs(j);

                if (pt.special) {
                    if (j - 1 >= -m) {
                        next[j - 1 + zero] = Math.min(next[j - 1 + zero], prev[j + zero]);
                    }
                } else {
                    if (j + 2 <= m) {
                        next[j + 2 + zero] = Math.min(next[j + 2 + zero], prev[j + zero]);
                    }
                    next[j + zero] = Math.min(next[j + zero], prev[j + zero]);
                }
            }

            long[] tmp = prev;
            prev = next;
            next = tmp;

            lastDist = pt.x;
        }

        long ans = prev[zero];
        if (ans >= inf / 2) {
            out.println(-1);
        } else {
            out.println(ans);
        }
    }
}

class Point {
    long x;
    boolean special;

    public Point(long x, boolean special) {
        this.x = x;
        this.special = special;
    }
}
