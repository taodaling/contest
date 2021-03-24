package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__245__Div__1_.E__Points_and_Segments;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EPointsAndSegments {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval(in.ri(), in.ri());
        }
        PriorityQueue<Interval> pq = new PriorityQueue<>(n, Comparator.comparingLong(x -> x.l));
        pq.addAll(Arrays.asList(intervals));
        while (pq.size() >= 2) {
            Interval a = pq.remove();
            Interval b = pq.remove();
            if (a.r < b.l) {
                pq.add(b);
                continue;
            }
            a.adj.add(b);
            b.adj.add(a);
            if (a.r >= b.r) {
                a.l = b.r + 1;
                if (a.l <= a.r) {
                    pq.add(a);
                }
            } else {
                b.l = a.r + 1;
                pq.add(b);
            }
        }
        for (Interval interval : intervals) {
            dfs(interval, 0);
            out.append(interval.v).append(' ');
        }
    }

    public void dfs(Interval root, int c) {
        if (root.v != -1) {
            return;
        }
        root.v = c;
        for (Interval interval : root.adj) {
            dfs(interval, c ^ 1);
        }
    }
}

class Interval {
    int v = -1;
    int l;
    int r;
    List<Interval> adj = new ArrayList<>();

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }
}