package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class Doors {
    Debug debug = new Debug(true);
    public int[] minIntersect(int[] a, int[] b, int t1, int t2) {
        IntervalIterator ai = new IntervalIterator(a, t1);
        IntervalIterator bi = new IntervalIterator(b, t2);
        ai.next();
        bi.next();
        int ans = 0;
        while (true) {
            int ll = Math.max(ai.l, bi.l);
            int rr = Math.min(ai.r, bi.r);
            ans = Math.max(rr - ll, ans);
            if (ai.r < bi.r) {
                if (ai.hasNext()) {
                    ai.next();
                } else {
                    break;
                }
            } else {
                if (bi.hasNext()) {
                    bi.next();
                } else {
                    break;
                }
            }
        }
        while (ai.hasNext()) {
            ai.next();
        }
        while (bi.hasNext()) {
            bi.next();
        }
        return new int[]{ans, ai.block + bi.block};
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int d = in.ri();
        int[] a = new int[n];
        int[] b = new int[m];
        in.populate(a);
        in.populate(b);
        PriorityQueue<Integer> pq1 = new PriorityQueue<>(n, Comparator.<Integer>naturalOrder());
        PriorityQueue<Integer> pq2 = new PriorityQueue<>(n, Comparator.<Integer>naturalOrder().reversed());
        for (int i = 0; i < n - 1; i++) {
            //a[i + 1] - a[i] <= 2t
            //t >= (a[i + 1] - a[i]) / 2
            int time = (a[i + 1] - a[i] + 1) / 2;
            pq1.add(time);
        }
        pq1.add(1);
        for (int i = 0; i < m - 1; i++) {
            int time = (b[i + 1] - b[i] + 1) / 2;
            pq2.add(time);
        }
        pq2.add(1);
        int inf = (int) 1e9;
        int ans = inf;
        int bestT1 = -1;
        int bestT2 = -1;
        int t1 = 1;
        int t2 = (int) 1e9;
        while (!pq1.isEmpty()) {
            t1 = pq1.remove();
            int[] res = minIntersect(a, b, t1, t2);
            while (res[0] > d) {
                if (pq2.isEmpty()) {
                    break;
                }
                t2 = pq2.remove();
                res = minIntersect(a, b, t1, t2);
            }
            if(res[0] <= d && res[1] < ans){
                ans = res[1];
                bestT1 = t1;
                bestT2 = t2;
            }
        }
        if(ans == inf){
            out.println("No solution");
        }else{
            debug.debug("best", ans);
            out.append(bestT1).append(' ').append(bestT2);
        }
    }
}

class IntervalIterator {
    int l;
    int r;
    int[] a;
    int t;
    int index;
    int block;

    public IntervalIterator(int[] a, int t) {
        this.a = a;
        this.t = t;
    }

    public boolean hasNext() {
        return index < a.length;
    }

    public void next() {
        l = a[index] - t;
        while (index + 1 < a.length && a[index + 1] - a[index] <= t + t) {
            index++;
        }
        r = a[index] + t;
        index++;
        block++;
    }
}