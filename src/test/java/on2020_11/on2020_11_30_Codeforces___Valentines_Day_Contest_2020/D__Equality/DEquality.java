package on2020_11.on2020_11_30_Codeforces___Valentines_Day_Contest_2020.D__Equality;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.FloorDivisionOptimizer;
import template.math.GenericModLog;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DEquality {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();

        PriorityQueue<Generator> leftPQ = new PriorityQueue<>((a, b) -> Integer.compare(a.l, b.l));
        PriorityQueue<Generator> rightPQ = new PriorityQueue<>((a, b) -> Integer.compare(a.r, b.r));

        List<Generator> generators = new ArrayList<>();
        int last = 0;
        for (int i = 0; i < x; i++) {
            int l = in.ri();
            int r = in.ri();
            if (last < l - 1) {
                generators.add(new Generator(last, l - 1, n, 1, 1));
            }
            last = r;
        }
        if (n > last) {
            generators.add(new Generator(last, n, n, 1, 1));
        }


        int y = in.ri();
        last = 0;
        for (int i = 0; i < y; i++) {
            int l = in.ri();
            int r = in.ri();
            if (last < l - 1) {
                generators.add(new Generator(last, l - 1, n, 0, 1));
            }
            last = r;
        }
        if (n > last) {
            generators.add(new Generator(last, n, n, 0, 1));
        }

        debug.elapse("prepare");
        for (Generator g : generators) {
            if (g.next()) {
                leftPQ.add(g);
                rightPQ.add(g);
            }
        }

        int m = generators.size();
        int cur = 0;
        if (m == 0) {
            out.println(n);
            return;
        }
        int sum = 0;
        last = 0;
        while (!rightPQ.isEmpty()) {
            int now = rightPQ.peek().r + 1;
            if (!leftPQ.isEmpty()) {
                now = Math.min(leftPQ.peek().l, now);
            }
            if (cur == m) {
                sum += now - last;
            }
            while (!rightPQ.isEmpty() && rightPQ.peek().r < now) {
                cur--;
                Generator head = rightPQ.remove();
                if (head.next()) {
                    leftPQ.add(head);
                    rightPQ.add(head);
                }
            }
            while (!leftPQ.isEmpty() && leftPQ.peek().l <= now) {
                cur++;
                leftPQ.remove();
            }
            last = now;
        }
        debug.elapse("handle");

        out.println(sum);
    }

}

class Generator {
    int a;
    int b;
    int l;
    int r;
    int la;
    int ra;
    int lb;
    int rb;
    int va;
    int vb;

    int limit;
    int plus;
    int rightshift;

    public Generator(int a, int b, int limit, int plus, int rightshift) {
        this.a = a;
        this.b = b;
        this.limit = limit;
        this.plus = plus;
        this.rightshift = rightshift;
        l = 1;
        r = 0;

        nextVA();
        nextVB();
    }

    public void nextVA() {
        la = ra + 1;
        va = a / la;
        ra = va == 0 ? limit : Math.min(a / va, limit);
        va = (va + plus) >> rightshift;
    }

    public void nextVB() {
        lb = rb + 1;
        vb = b / lb;
        rb = vb == 0 ? limit : Math.min(b / vb, limit);
        vb = (vb + plus) >> rightshift;
    }

    public boolean next() {
        while (r < limit) {
            if (ra <= r) {
                nextVA();
            }
            if (rb <= r) {
                nextVB();
            }
            if (va < vb) {
                nextVB();
            } else if (va > vb) {
                nextVA();
            } else {
                l = Math.max(la, lb);
                r = Math.min(ra, rb);
                if (l <= r) {
                    return true;
                }
            }
        }
        return false;
    }

}