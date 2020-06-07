package on2020_06.on2020_06_06_Google_Coding_Competitions___Round_3_2020___Code_Jam_2020.Thermometers;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Thermometers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        int k = in.readInt();
        int n = in.readInt();
        int[] x = new int[n];
        int[] t = new int[n];
        in.populate(x);
        in.populate(t);

        boolean same = true;
        for (int i = 1; i < n; i++) {
            if (t[i - 1] != t[i]) {
                same = false;
            }
        }

        if (same) {
            out.println(1);
            return;
        }

        Deque<Loop> dq = new ArrayDeque<>(n);
        for (int i = 1; i <= n; i++) {
            Loop loop = new Loop();
            loop.l = x[i - 1];
            loop.r = x[i % n];
            loop.t = t[i - 1];
            if (!dq.isEmpty() && dq.peekLast().t == loop.t) {
                dq.peekLast().r = loop.r;
                continue;
            }
            dq.addLast(loop);
        }


        dq.peekLast().r += k;
        Loop first = dq.removeFirst();
        Interval head = new Interval(first.l, first.r, null, false, true, false);
        Interval last = head;
        int ans = 1;
        for (Loop lp : dq) {
            //reflect
            int rl = mirror(last.l, lp.l);
            int rr = mirror(last.r, lp.l);
            int fixRl = Math.min(rl, lp.r);
            int fixRr = Math.max(rr, lp.l);
            if (fixRl <= fixRr) {
                //need a new guy
                Interval create = new Interval(lp.l, lp.r, null, false, true, false);
                last = create;
                ans += 2;
            } else {
                //reuse
                if (!last.rev) {
                    last.p.l += rl - fixRl;
                    last.p.r -= rr - fixRr;
                    if (fixRl == lp.r) {
                        last.p.encloseL = false;
                    }
                } else {
                    last.p.r -= rl - fixRl;
                    last.p.l += rr - fixRr;
                }

                Interval create = new Interval(fixRr, fixRl, last.p, !last.rev, true, true);
                last = create;
                ans++;
            }
        }

        //two case
        last.l -= k;
        last.r -= k;
        int rl = mirror(last.l, first.l);
        int rr = mirror(last.r, first.l);
        int fixRl = Math.min(rl, head.r);
        int fixRr = Math.max(rr, head.l);
        if (fixRl <= fixRr) {
            ans++;
        } else if (last.p == head) {
            if (!last.rev) {
                if ((dist(last.l, first.l) > dist(first.l, head.l) || dist(last.l, first.l) == dist(first.l, head.l) && head.encloseL) &&
                        dist(last.r, first.l) < dist(first.l, head.r)) {

                } else {
                    ans++;
                }
            } else {
                if (dist(last.r, first.l) == dist(first.l, head.l)){

                }else{
                    ans++;
                }
            }
        }

        out.println(ans);
    }

    public int dist(int l, int r) {
        return r - l;
    }

    public int mirror(int l, int center) {
        return 2 * center - l;
    }
}

class Interval {
    int l;
    int r;
    boolean encloseL;
    boolean encloseR;

    Interval p;
    boolean rev;

    public Interval(int l, int r, Interval p, boolean rev, boolean encloseL, boolean encloseR) {
        this.l = l;
        this.r = r;
        this.rev = rev;
        this.encloseL = encloseL;
        this.encloseR = encloseR;
        if (p != null) {
            this.p = p;
        } else {
            this.p = this;
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", l, r);
    }
}

class Loop {
    int l;
    //exclude
    int r;
    int t;
}
