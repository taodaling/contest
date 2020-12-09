package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.PriorityQueue;

public class ETwoEditorials {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        Interval[] intervals = new Interval[m];
        for (int i = 0; i < m; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.ri() - 1;
            intervals[i].r = in.ri() - 1;
        }

        FastPQ[] pq = new FastPQ[4];
        for (int i = 0; i < 4; i++) {
            pq[i] = new FastPQ();
        }
        long ans = 0;
        for (int a = k - 1; a < n; a++) {
            for (int i = 0; i < 4; i++) {
                pq[i].clear();
            }
            long maintain = 0;
            for (Interval x : intervals) {
                x.next = x.l - 1;
                pq[3].add(x);
                maintain += x.cover = cover(a - k + 1, a, x.l, x.r);
            }
            optimize(pq, k, a);
            ans = Math.max(ans, maintain);
            for (int b = a + 1; b < n; b++) {
                maintain += pq[0].size() - pq[2].size();
                ans = Math.max(ans, maintain);
                optimize(pq, k, b);
            }
        }

        out.println(ans);
    }

    public void optimize(FastPQ[] pq, int k, int i) {
        while (!pq[3].isEmpty(i) && pq[3].peek(i).next <= i) {
            Interval head = pq[3].remove(i);
            head.next = Math.min(head.r, head.l + k - 1);
            pq[0].add(head);
        }
        //0
        while (!pq[0].isEmpty(i) && pq[0].peek(i).next <= i) {
            Interval head = pq[0].remove(i);
            head.next = Math.max(head.r, head.l + k - 1);
            pq[1].add(head);
        }
        //1
        while (!pq[1].isEmpty(i) && pq[1].peek(i).next <= i) {
            Interval head = pq[1].remove(i);
            head.next = cover(head.l, head.r, i - k + 1, i) - head.cover + i;
            pq[2].add(head);
        }
        while (!pq[2].isEmpty(i) && pq[2].peek(i).next <= i) {
            Interval head = pq[2].remove(i);
        }
    }

    public int cover(int ll, int rr, int l, int r) {
        return Math.max(0, Math.min(rr, r) - Math.max(ll, l) + 1);
    }
}

class FastPQ {
    static int max = 2010;
    MultiWayStack<Interval> stack = new MultiWayStack<>(max, max);
    int cur = 0;
    int size = 0;

    public void clear() {
        stack.clear();
        cur = 0;
        size = 0;
    }

    public void add(Interval interval) {
        int q = interval.next;
        q = Math.max(q, cur);
        q = Math.min(q, max - 1);
        stack.addLast(q, interval);
        size++;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty(int i) {
        return !reindex(i);
    }

    private boolean reindex(int i) {
        while (stack.isEmpty(cur) && cur + 1 <= i) {
            cur++;
        }
        return !stack.isEmpty(cur);
    }

    public Interval peek(int i) {
        assert size > 0;
        reindex(i);
        return stack.peekLast(cur);
    }

    public Interval remove(int i) {
        assert size > 0;
        size--;
        reindex(i);
        return stack.removeLast(cur);
    }
}

class Interval {
    int l;
    int r;

    int next;
    int cover;
}
