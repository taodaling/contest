package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.math.Modular;
import template.primitve.generated.datastructure.*;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.Debug;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CInstantNoodles {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] right = new long[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            right[i] = in.readLong();
            sum += right[i];
        }
        IntegerList[] indeg = new IntegerList[n];
        for (int i = 0; i < n; i++) {
            indeg[i] = new IntegerList();
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            indeg[b].add(a);
        }

        for (int i = 0; i < n; i++) {
            indeg[i].unique();
        }

        for (int i = 0; i < n; i++) {
            if (indeg[i].isEmpty()) {
                right[i] = 0;
            }
        }

        Modular mod = new Modular(1e9 + 7);
        LongHashMap map = new LongHashMap(n, false);
        for (int i = 0; i < n; i++) {
            long key = DigitUtils.asLong(
                    hash(indeg[i], mod, 31),
                    hash(indeg[i], mod, 41)
            );
            Node node = map.get(key);
            if (node == null) {
                node = new Node();
                node.indeg = indeg[i].size();
                node.sum = 0;
                map.put(key, node);
            }
            node.sum += right[i];
        }

        long ans = 0;
        for (LongObjectEntryIterator<Node> iterator = map.iterator();
             iterator.hasNext(); ) {
            iterator.next();
            ans = GCDs.gcd(ans, iterator.getEntryValue().sum);
        }
        out.println(ans);
    }

    public int hash(IntegerList list, Modular mod, int x) {
        int h = mod.valueOf(1);
        for (int i = 0, end = list.size(); i < end; i++) {
            h = mod.mul(h, x);
            h = mod.plus(h, list.get(i));
        }
        return h;
    }
}

class Node {
    int indeg;
    long sum;

    @Override
    public String toString() {
        return String.format("%d|%d", indeg, sum);
    }
}