package on2020_02.on2020_02_20_Codeforces_Round__286__Div__1_.D__Mr__Kitayuta_s_Colorful_Graph;



import dp.Lis;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DMrKitayutasColorfulGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Color[] colors = new Color[m + 1];
        for (int i = 1; i <= m; i++) {
            colors[i] = new Color();
            colors[i].v = i;
        }
        Map<Color, Node>[] maps = new Map[n + 1];
        for (int i = 1; i <= n; i++) {
            maps[i] = new IdentityHashMap<>(0);
        }

        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            Node.merge(get(maps[a], colors[c]), get(maps[b], colors[c]));
        }

        int q = in.readInt();
        LongHashMap map = new LongHashMap(q, false);
        for (int i = 0; i < q; i++) {
            int aId = in.readInt();
            int bId = in.readInt();
            long key = DigitUtils.asLong(Math.min(aId, bId),
                    Math.max(aId, bId));
            int sum = (int) map.getOrDefault(key, -1);
            if (sum < 0) {
                sum = 0;
                Map<Color, Node> a = maps[aId];
                Map<Color, Node> b = maps[bId];
                if (a.size() > b.size()) {
                    Map<Color, Node> tmp = a;
                    a = b;
                    b = tmp;
                }

                for (Map.Entry<Color, Node> entry : a.entrySet()) {
                    Node x = entry.getValue();
                    Node y = b.get(entry.getKey());
                    if (y != null && y.find() == x.find()) {
                        sum++;
                    }
                }
                map.put(key, sum);
            }
            out.println(sum);
        }
    }


    Node get(Map<Color, Node> map, Color c) {
        Node node = map.get(c);
        if (node == null) {
            node = new Node();
            map.put(c, node);
        }
        return node;
    }
}

class Color implements Comparable<Color> {
    int v;

    @Override
    public int compareTo(Color o) {
        return Integer.compare(v, o.v);
    }
}

class Node {
    int rank;
    Node p = this;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }
}
