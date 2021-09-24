package on2021_07.on2021_07_20_CodeChef___June_Cook_Off_2021_Division_1.Clamp_Paths_on_Tree;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClampPathsOnTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            int p = in.ri() - 1;
            if (p == -1) {
                continue;
            }
            nodes[p].adj.add(nodes[i]);
            nodes[i].adj.add(nodes[p]);
        }
        DSUExt dsu = new DSUExt(n, nodes);
        dsu.init();
        dsu.cmp = Comparator.comparingInt(x -> x.id);
        //min tree
        for (int i = n - 1; i >= 0; i--) {
            Node root = nodes[i];
            for (Node node : root.adj) {
                if (node.id < root.id) {
                    continue;
                }
                root.adjMin.add(dsu.min[dsu.find(node.id)]);
                dsu.merge(root.id, node.id);
            }
        }
        dsu.init();
        dsu.cmp = Comparator.comparingInt(x -> -x.id);
        //max tree
        for (int i = 0; i < n; i++) {
            Node root = nodes[i];
            for (Node node : root.adj) {
                if (node.id > root.id) {
                    continue;
                }
                root.adjMax.add(dsu.min[dsu.find(node.id)]);
                dsu.merge(root.id, node.id);
            }
        }

        dfsForTimestamp(nodes[0]);
        bit = new IntegerBIT(n);
        dfsForSolve(nodes[n - 1]);
        out.println(ans);
    }

    int time = 0;

    public void dfsForTimestamp(Node root) {
        root.open = ++time;
        for (Node node : root.adjMin) {
            dfsForTimestamp(node);
        }
        root.close = time;
    }

    long ans = 0;
    IntegerBIT bit;

    Debug debug = new Debug(false);
    public void dfsForSolve(Node root) {
        int contrib = bit.query(root.open, root.close);
        debug.debug("root", root);
        debug.debug("contrib", contrib);
        ans += contrib;
        bit.update(root.open, 1);
        for (Node node : root.adjMax) {
            dfsForSolve(node);
        }
        bit.update(root.open, -1);
    }
}

class DSUExt extends DSU {
    Node[] min;
    Comparator<Node> cmp;
    Node[] nodes;

    public DSUExt(int n, Node[] nodes) {
        super(n);
        this.min = nodes.clone();
        this.nodes = nodes;
    }

    @Override
    public void init(int n) {
        super.init(n);
        System.arraycopy(nodes, 0, min, 0, min.length);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        if (cmp.compare(min[b], min[a]) < 0) {
            min[a] = min[b];
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    List<Node> adjMax = new ArrayList<>();
    List<Node> adjMin = new ArrayList<>();
    int id;
    int open;
    int close;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
