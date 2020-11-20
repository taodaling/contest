package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashSet;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class BGraphSubsetProblem {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        hs.clear();
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
            add(a.id, b.id);
            a.deg++;
            b.deg++;
        }
        boolean possible = (long) k * (k - 1) / 2 <= m;
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.deg == b.deg ? Integer.compare(a.id, b.id) : Integer.compare(a.deg, b.deg));
        set.addAll(Arrays.asList(nodes));
        Node[] near = new Node[n];
        while (!set.isEmpty()) {
            Node first = set.pollFirst();
            debug.debug("first", first);
            if (first.deg >= k) {
                set.add(first);
                out.append(1).append(' ').append(set.size()).println();
                for (Node node : set) {
                    out.append(node.id + 1).append(' ');
                }
                out.println();
                return;
            }
            int wpos = 0;
            first.deleted = true;
            for (Node node : first.adj) {
                if (node.deleted) {
                    continue;
                }
                if (possible && node.deg >= k - 1) {
                    near[wpos++] = node;
                }
                set.remove(node);
                node.deg--;
                set.add(node);
            }
            if (possible && wpos == k - 1) {
                boolean valid = true;
                for (int i = 0; i < k - 1 && valid; i++) {
                    for (int j = 0; j < i && valid; j++) {
                        valid = valid && contain(near[i].id, near[j].id);
                    }
                }
                if (valid) {
                    out.println(2);
                    near[wpos++] = first;
                    for (int i = 0; i < k; i++) {
                        out.append(near[i].id + 1).append(' ');
                    }
                    out.println();
                    return;
                }
            }
        }
        out.println(-1);
    }

    LongHashSet hs = new LongHashSet((int) 2e5, true);

    public void add(int u, int v) {
        hs.add(eId(u, v));
    }

    public boolean contain(int u, int v) {
        return hs.contain(eId(u, v));
    }

    public long eId(int u, int v) {
        if (u > v) {
            int tmp = u;
            u = v;
            v = tmp;
        }
        return DigitUtils.asLong(u, v);
    }
}

class Node {
    boolean deleted = false;
    int id;
    List<Node> adj = new ArrayList<>();
    int deg;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}