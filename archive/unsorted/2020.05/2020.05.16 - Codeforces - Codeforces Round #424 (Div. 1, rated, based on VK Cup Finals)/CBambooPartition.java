package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.utils.Debug;

import java.util.Arrays;
import java.util.PriorityQueue;

public class CBambooPartition {
    //Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        int[] a = new int[n];
        Single[] singles = new Single[n];
        long right = k;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            singles[i] = new Single(a[i], i);
            right += a[i];
        }


        long ans = 0;
        Machine mac = new Machine(singles);
        do {
//            debug.debug("mac.l", mac.l);
//            debug.debug("mac.r", mac.r);
//            debug.debug("mac.sum", mac.sum);
            long r = right / mac.sum;
            if (r < mac.l) {
                continue;
            }
            r = Math.min(r, mac.r);
            ans = Math.max(r, ans);
        } while (mac.next());

        out.println(ans);
    }
}

class Single {
    int a;
    long l;
    long r;
    int x;
    int id;

    public Single(int a, int id) {
        this.id = id;
        this.a = a;
        l = r = 1;
        x = a;
    }

    public void next() {
        l = r + 1;
        x = (int) DigitUtils.ceilDiv(a, l);
        if (x == 1) {
            r = (long) 1e15;
        } else {
            r = DigitUtils.ceilDiv(a, x - 1) - 1;
        }
    }

    @Override
    public String toString() {
        return String.format("a=%d, l=%d, r=%d", a, l, r);
    }
}

class Machine {
    PriorityQueue<Single> pq;
    static long inf = (long) 1e15;
    long l = 1;
    long r = inf;
    long sum;
    int[] vals;

    public Machine(Single[] singles) {
        pq = new PriorityQueue<>(singles.length, (a, b) -> Long.compare(a.l, b.l));
        vals = new int[singles.length];
        for (Single s : singles) {
            vals[s.id] = s.x;
            sum += s.x;
            s.next();
            r = Math.min(r, s.l - 1);
        }
        pq.addAll(Arrays.asList(singles));
    }

    public boolean next() {
        if (pq.peek().l >= inf) {
            return false;
        }
        Single top = pq.poll();
        l = top.l;
        sum -= vals[top.id];
        sum += vals[top.id] = top.x;
        top.next();
        pq.add(top);
        r = pq.peek().l - 1;

        return true;
    }
}
