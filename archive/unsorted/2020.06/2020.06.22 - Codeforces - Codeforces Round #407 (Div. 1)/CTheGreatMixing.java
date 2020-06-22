package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class CTheGreatMixing {
    int limit = 1000;
    int leftBound = -(limit * limit + 5 * limit);
    int rightBound = (limit * limit + 5 * limit);
    int len = rightBound - leftBound + 1;
    int zero = -leftBound;
    int n;

    int globalApply(int i) {
        return i + zero;
    }

    int globalInverse(int i) {
        return i - zero;
    }

    int localApply(int i) {
        return i + n;
    }

    int localInverse(int i) {
        return i - n;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int k = in.readInt();

        BitSet move = new BitSet(limit + 1);
        for (int i = 0; i < k; i++) {
            int x = in.readInt();
            move.set(x);
        }


        int[] dist = new int[len];
        BitSet alive = new BitSet(len);
        alive.fill(true);

        BitSet interval = new BitSet(limit + 1);
        IntegerDeque dq = new IntegerDequeImpl(len);
        for (int i = 0; i <= limit; i++) {
            if (move.get(i)) {
                int j = globalApply(localInverse(i));
                dist[j] = 1;
                alive.clear(j);
                dq.addLast(j);
            }
        }

        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            int inv = globalInverse(head);
            if (Math.abs(inv) > limit * limit + limit) {
                continue;
            }
            if (head == zero) {
                break;
            }
            int l = head - n;
            int r = l + limit;
            interval = alive.interval(l, r);
            interval.and(move);
            for (int i = interval.nextSetBit(0); i < interval.capacity(); i = interval.nextSetBit(i + 1)) {
                //0 => head - n
                int j = i + head - n;
                alive.clear(j);
                dist[j] = dist[head] + 1;
                dq.addLast(j);
            }
        }

        if (alive.get(zero)) {
            out.println(-1);
            return;
        }

        out.println(dist[zero]);

    }

}
