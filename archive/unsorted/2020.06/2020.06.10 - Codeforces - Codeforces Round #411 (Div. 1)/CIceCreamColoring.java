package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;
import java.util.stream.Collectors;

public class CIceCreamColoring {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            int s = in.readInt();
            for (int j = 0; j < s; j++) {
                int c = in.readInt() - 1;
                nodes[i].set.add(c);
            }
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dsu = new DSU(m);
        List<Integer> list = dfs(nodes[0], null);
        HashMap<Integer, Integer> colors = new HashMap<>(m);
        debug.debug("list", list);
        for (int i = 0; i < list.size(); i++) {
            colors.put(dsu.find(list.get(i)), i + 1);
        }
        debug.debug("colors", colors);
        int ans = Math.max(1, colors.size());
        out.println(ans);
        for (int i = 0; i < m; i++) {
            int val = colors.getOrDefault(dsu.find(i), 1);
            out.append(val).append(' ');
        }
    }

    DSU dsu;

    public List<Integer> dfs(Node root, Node p) {
        List<Integer> extra = new ArrayList<>();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            List<Integer> child = dfs(node, root);
            for (Integer x : root.set) {
                if (child.isEmpty()) {
                    break;
                }
                if (node.set.contains(x)) {
                    continue;
                }
                dsu.merge(child.get(child.size() - 1), x);
                child.remove(child.size() - 1);
            }

            if (child.size() > extra.size()) {
                List<Integer> tmp = child;
                child = extra;
                extra = tmp;
            }
            for (int i = 0; i < child.size(); i++) {
                dsu.merge(extra.get(i), child.get(i));
            }
        }
        Set<Integer> pSet = Collections.emptySet();
        if (p != null) {
            pSet = p.set;
        }

        for (Integer x : root.set) {
            if (pSet.contains(x)) {
                continue;
            }
            extra.add(x);
        }
        return extra;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Set<Integer> set = new HashSet<>();
}
