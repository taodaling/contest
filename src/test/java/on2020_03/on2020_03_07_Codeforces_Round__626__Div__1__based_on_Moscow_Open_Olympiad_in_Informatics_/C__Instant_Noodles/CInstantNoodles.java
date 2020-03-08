package on2020_03.on2020_03_07_Codeforces_Round__626__Div__1__based_on_Moscow_Open_Olympiad_in_Informatics_.C__Instant_Noodles;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongObjectEntryIterator;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.utils.Debug;

import java.util.ArrayList;
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

        Modular mod = new Modular(1e9 + 7);
        LongObjectHashMap<Node> map = new LongObjectHashMap<>(n, false);
        for (int i = 0; i < n; i++) {
            if(indeg[i].isEmpty()){
                continue;
            }
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

        List<Node> list = new ArrayList<>(map.size());
        for (LongObjectEntryIterator<Node> iterator = map.iterator();
             iterator.hasNext(); ) {
            iterator.next();
            list.add(iterator.getEntryValue());
        }

        Node[] nodes = list.toArray(new Node[0]);
        debug.debug("map", map);
        long g = sum;
        while (true) {
            Node index = null;
            for (Node node : nodes) {
                if (node.sum % g == 0) {
                    continue;
                }
                if (index == null || node.indeg < index.indeg) {
                    index = node;
                }
            }
            if (index == null) {
                break;
            }
            debug.debug("index.sum", index.sum);
            g = GCDs.gcd(g, sum - index.sum);
        }

        out.println(g);
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