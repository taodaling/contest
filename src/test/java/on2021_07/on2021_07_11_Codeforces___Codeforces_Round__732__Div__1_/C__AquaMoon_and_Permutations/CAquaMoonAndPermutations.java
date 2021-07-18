package on2021_07.on2021_07_11_Codeforces___Codeforces_Round__732__Div__1_.C__AquaMoon_and_Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

import java.util.*;
import java.util.stream.Collectors;

public class CAquaMoonAndPermutations {
    int mod = 998244353;
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n * 2];
        for (int i = 0; i < n * 2; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].seq = in.ri(n);
        }
        XorDeltaDSU dsu = new XorDeltaDSU(n * 2);

        for (int i = 0; i < n * 2; i++) {
            for (int j = i + 1; j < n * 2; j++) {
                if (same(nodes[i].seq, nodes[j].seq)) {
                    nodes[i].adj.add(nodes[j]);
                    nodes[j].adj.add(nodes[i]);
                    dsu.merge(i, j, 1);
                }
            }
        }
        int odd = 0;
        int cc = 0;
        for (int i = 0; i < 2 * n; i++) {
            if (dsu.find(i) == i) {
                cc++;
                if (dsu.odd[i]) {
                    odd++;
                }
            }
        }
        long ans = power.pow(2, odd + cc);
        out.println(ans);
        Map<Integer, List<Node>> groupBy = Arrays.stream(nodes).collect(Collectors.groupingBy(x -> dsu.find(x.id)));
        for (List<Node> list : groupBy.values()) {
            boolean suss = false;
            while (!suss) {
                for (Node node : list) {
                    node.visited = false;
                    node.select = false;
                }
                Collections.shuffle(list);
                add(list.get(0));
                int added = 0;
                for (Node node : list) {
                    if (node.select) {
                        added++;
                    }
                }
                suss = added == list.size() / 2;
            }
        }

        //  assert res.size() == n;
        for (Node node : nodes) {
            if (!node.select) {
                continue;
            }
            out.append(node.id + 1).append(' ');
        }
        out.println();
    }

    public void add(Node root) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        root.select = true;
        for (Node node : root.adj) {
            if (node.select) {
                root.select = false;
            }
        }
        for (Node node : root.adj) {
            add(node);
        }
    }

    public boolean same(int[] a, int[] b) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            if (a[i] == b[i]) {
                return true;
            }
        }
        return false;
    }
}

class XorDeltaDSU {
    int[] p;
    int[] size;
    int[] delta;
    boolean valid;
    boolean[] odd;

    public boolean valid() {
        return valid;
    }

    public XorDeltaDSU(int n) {
        p = new int[n];
        size = new int[n];
        delta = new int[n];
        odd = new boolean[n];
        init();
    }

    public void init() {
        init(p.length);
    }

    public void init(int n) {
        for (int i = 0; i < n; i++) {
            p[i] = i;
            size[i] = 0;
            delta[i] = 0;
            odd[i] = false;
        }
        valid = true;
    }

    public int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] ^= delta[p[a]];
        return p[a] = p[p[a]];
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b) {
        find(a);
        find(b);
        return delta[a] ^ delta[b];
    }

    public int deltaRoot(int a) {
        find(a);
        return delta[a];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d ^ delta[a] ^ delta[b];
        a = find(a);
        b = find(b);
        if (a == b) {
            if (d != 0) {
                odd[a] = true;
            }
            return;
        }
        if (size[a] < size[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        size[a] += size[b];
        p[b] = a;
        delta[b] = d;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int[] seq;
    int id;
    boolean select;
    boolean visited;
}