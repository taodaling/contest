package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CColorfulTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        if(!in.hasMore()){
            throw new UnknownError();
        }

        subtract = 0;

        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].color = in.readInt();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null);
        for (int i = 1; i <= n; i++) {
            int value = nodes[0].cnts.get(i);
            remove(value);
        }

        long ans = pick2(n) * n - subtract;
        out.printf("Case #%d: %d", testNumber, ans).println();
    }

    long subtract = 0;

    public long pick2(long x) {
        return x * (x - 1) / 2;
    }

    public void remove(long b) {
        subtract += pick2(b);
    }

    public void dfs(Node root, Node p) {
        root.cnts.modAll(1);
        root.cnts.modOne(root.color, -1);
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            int value = node.cnts.get(root.color);
            node.cnts.modOne(root.color, -value);
            remove(value);
            root.cnts = MapHolder.merge(root.cnts, node.cnts);
        }
    }
}

class MapHolder {
    Map<Integer, Integer> cnt;
    int all;

    public MapHolder(Map<Integer, Integer> cnt) {
        this.cnt = cnt;
    }

    public MapHolder() {
        this(new HashMap<>());
    }

    public void modAll(int x) {
        all += x;
    }

    public void modOne(Integer c, int x) {
        cnt.put(c, cnt.getOrDefault(c, 0) + x);
    }

    public int get(Integer c) {
        return cnt.getOrDefault(c, 0) + all;
    }

    public static MapHolder merge(MapHolder a, MapHolder b) {
        if (a.cnt.size() > b.cnt.size()) {
            MapHolder holder = a;
            a = b;
            b = holder;
        }
        b.all += a.all;
        for (Map.Entry<Integer, Integer> entry : a.cnt.entrySet()) {
            b.modOne(entry.getKey(), entry.getValue());
        }
        return b;
    }
}

class Node {
    int color;
    List<Node> next = new ArrayList<>();
    MapHolder cnts = new MapHolder();
}
