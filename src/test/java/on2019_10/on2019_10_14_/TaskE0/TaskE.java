package on2019_10.on2019_10_14_.TaskE0;




import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import template.FastInput;

public class TaskE {

    int inf = (int) 1e8;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Node x = nodes[in.readInt()];
        Node y = nodes[in.readInt()];

        List<Query> querys = new ArrayList<>(n);
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.red.add(b);
            b.red.add(a);
            Query q = new Query();
            q.a = a;
            q.b = b;
            querys.add(q);
            a.lcaQueries.add(q);
            b.lcaQueries.add(q);
        }

        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.blue.add(b);
            b.blue.add(a);
        }

        dfsForDepth(y, null, 0);
        dfsForLca(y, null);

        for (Query q : querys) {
            if (q.a.depth + q.b.depth - 2 * q.lca.depth >= 3) {
                q.a.escape = q.b.escape = true;
            }
        }

        int escape = escape(x, null, 0);
        if (escape == inf) {
            out.println(-1);
        } else {
            out.println(escape * 2);
        }
    }

    public void dfsForLca(Node root, Node fa) {
        root.blue.remove(fa);
        root.lca = root;
        root.visited = true;
        for (Node node : root.blue) {
            if (node == fa) {
                continue;
            }
            dfsForLca(node, root);
            Node.merge(node, root);
            root.find().lca = root;
        }

        for (Query q : root.lcaQueries) {
            Node other = q.a == root ? q.b : q.a;
            if (!other.visited) {
                continue;
            }
            q.lca = other.find().lca;
        }
    }

    public int escape(Node root, Node fa, int depth) {
        root.red.remove(fa);
        if (root.depth <= depth) {
            return root.depth;
        }
        if (root.escape) {
            return inf;
        }
        int max = root.depth;
        for (Node node : root.red) {
            max = Math.max(max, escape(node, root, depth + 1));
        }
        return max;
    }

    public void dfsForDepth(Node root, Node fa, int depth) {
        root.blue.remove(fa);
        root.depth = depth;
        for (Node node : root.blue) {
            dfsForDepth(node, root, depth + 1);
        }
    }
}


class Query {
    Node a;
    Node b;
    Node lca;
}


class Node {
    List<Node> red = new ArrayList<>();
    List<Node> blue = new ArrayList<>();
    List<Query> lcaQueries = new ArrayList<>();
    boolean escape;
    int depth;
    int id;

    boolean visited = false;
    Node p = this;
    int rank;
    Node lca;

    Node find() {
        return p == p.p ? p : (p = p.find());
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
