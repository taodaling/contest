package on2020_06.on2020_06_08_Codeforces___Codeforces_Round__647__Div__1____Thanks__Algo_Muse_.E__James_and_the_Chase;



import template.datastructure.FixedMinCollection;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class EJamesAndTheChase {
    RandomWrapper rw = new RandomWrapper();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
        }

        Node root = null;

        for (int i = 0; i < 50; i++) {
            Node randRoot = nodes[rw.nextInt(0, n - 1)];
            valid = true;
            for (Node node : nodes) {
                node.visited = false;
                node.instk = false;
            }

            dfs(randRoot, 0);
            for (Node node : nodes) {
                valid = valid && node.visited;
            }

            if (!valid) {
                continue;
            } else {
                root = randRoot;
                break;
            }
        }

        if (root == null) {
            out.println(-1);
            return;
        }

        //dfs and find root
        dpOnTree(root);
        root.ok = true;
        countOnTree(root);

        List<Node> interestingNodes = new ArrayList<>(n);
        for (Node node : nodes) {
            if (node.ok) {
                interestingNodes.add(node);
            }
        }

        if (interestingNodes.size() * 5 < n) {
            out.println(-1);
            return;
        }

        //out.println(interestingNodes.size());
        for (Node node : interestingNodes) {
            out.append(node.id + 1).append(' ');
        }
        out.println();
    }

    Comparator<Node> sortByDepth = (a, b) -> -Integer.compare(a.depth, b.depth);

    public void countOnTree(Node root) {
        if (root.top != null && root.top.ok) {
            root.ok = true;
        }
        for (Node node : root.adj) {
            if (node.depth != root.depth + 1) {
                continue;
            }
            countOnTree(node);
        }
    }

    public void dpOnTree(Node root) {
        for (Node node : root.adj) {
            if (node.depth != root.depth + 1) {
                //to ancestor
                root.pq.add(node);
            } else {
                dpOnTree(node);
                root.pq.addAll(node.pq);
            }
        }

        while (!root.pq.isEmpty() && root.pq.peekMax().depth >= root.depth) {
            root.pq.popMax();
        }

        if (root.pq.size() == 1) {
            root.top = root.pq.iterator().next();
        }
    }

    boolean valid;

    public void dfs(Node root, int depth) {
        if (root.visited) {
            valid = valid && root.instk;
            return;
        }

        root.visited = true;
        root.instk = true;
        root.depth = depth;

        for (Node node : root.adj) {
            dfs(node, depth + 1);
        }

        root.instk = false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    boolean visited;
    boolean instk;
    int depth;
    int size;
    Node top;
    boolean ok;

    static Comparator<Node> sortByDepth = (a, b) -> Integer.compare(a.depth, b.depth);
    FixedMinCollection<Node> pq = new FixedMinCollection<>(2, sortByDepth);

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
