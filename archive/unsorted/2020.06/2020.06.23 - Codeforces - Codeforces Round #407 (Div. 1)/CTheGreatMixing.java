package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;

public class CTheGreatMixing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        int limit = 1000;
        BitSet edge = new BitSet(limit + 1);
        for (int i = 0; i < k; i++) {
            edge.set(in.readInt());
        }

        int leftBound = -limit * 3;
        int rightBound = limit * 3;
        BitSet vertex = new BitSet(rightBound - leftBound + 1);
        int zero = -leftBound;
        vertex.fill(true);
        int[] dist = new int[vertex.capacity()];
        Arrays.fill(dist, -1);
        IntegerDeque dq = new IntegerDequeImpl(vertex.capacity());
        for (int i = 0; i <= limit; i++) {
            if (edge.get(i)) {
                int j = i - n + zero;
                vertex.clear(j);
                dq.addLast(j);
                dist[j] = 1;
            }
        }

        BitSet interval = new BitSet(2000);
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            int inv = head - zero;
            if (inv == 0) {
                break;
            }
            if (inv < -limit || inv > limit) {
                continue;
            }
            int l = head - n;
            int r = l + limit;
            interval.copyInterval(vertex, l, r);
            interval.and(edge);
            for (int i = interval.nextSetBit(0); i < interval.capacity(); i = interval.nextSetBit(i + 1)) {
                int j = i + l;
                vertex.clear(j);
                dq.addLast(j);
                dist[j] = dist[head] + 1;
            }
        }

        out.println(dist[zero]);
    }
}
