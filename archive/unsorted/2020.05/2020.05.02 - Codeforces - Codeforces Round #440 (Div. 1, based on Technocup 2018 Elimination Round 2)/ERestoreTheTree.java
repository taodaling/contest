package contest;

import graphs.lca.Lca;
import template.binary.Log2;
import template.datastructure.MultiWayStack;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.Debug;

import java.util.*;

public class ERestoreTheTree {
    List<UndirectedEdge>[] g;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        List<Node> known = new ArrayList<>(m);
        for (int i = 0; i < m && valid; i++) {
            int[] dist = new int[n];
            Node node = null;
            for (int j = 0; j < n; j++) {
                dist[j] = in.readInt();
                if (dist[j] == 0) {
                    if (node != null || nodes[j].dist != null) {
                        valid = false;
                    }
                    node = nodes[j];
                    node.dist = dist;
                }
            }
            if (node == null) {
                valid = false;
            }
            known.add(node);
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        for (Node node : known) {
            node.heights = new IntegerMultiWayStack(n, n);
            for (int i = 0; i < n; i++) {
                node.heights.addLast(node.dist[i], i);
            }
        }


        Node root = known.get(0);
        for (Node node : nodes) {
            node.depth = root.dist[node.id];
        }
        for (int i = 0; i < n; i++) {
            for (IntegerIterator iterator = root.heights.iterator(i); iterator.hasNext(); ) {
                int next = iterator.next();
                if (nodes[next].dist != null) {
                    build(root, nodes[next], nodes[next]);
                }
            }
        }

        Deque<Node> dq = new ArrayDeque<>(n);
        for (int i = 1; i < n && valid; i++) {
            for (IntegerIterator iterator = root.heights.iterator(i); iterator.hasNext(); ) {
                Node next = nodes[iterator.next()];
                if (next.visited) {
                    continue;
                }
                int lca = (int) -1e9;
                Node which = null;
                for (Node node : known) {
                    int local = (i + root.dist[node.id] - node.dist[next.id]) / 2;
                    if (lca < local) {
                        lca = local;
                        which = node;
                    }
                }

                if (which.depth - lca < 0) {
                    valid = false;
                    break;
                }
                Node lcaNode = toDepth(which, which.depth - lca);
                next.parent = lcaNode.chain;
                setFather(next, next.parent);
                lcaNode.cand = next;
                dq.add(lcaNode);
            }

            while(!dq.isEmpty()){
                Node head = dq.removeFirst();
                head.chain = head.cand;
            }
        }

        for (Node node : nodes) {
            if (node != root && node.parent == null) {
                valid = false;
            }
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        g = Graph.createUndirectedGraph(n);
        for (Node node : nodes) {
            if (node == root) {
                continue;
            }
            Graph.addUndirectedEdge(g, node.id, node.parent.id);
        }

        debug.debug("root", root);
        debug.debug("g", g);

        lca = new LcaOnTree(g, root.id);

        for (Node node : known) {
            for (Node x : nodes) {
                if (dist(node.id, x.id) != node.dist[x.id]) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                break;
            }
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        for (Node node : nodes) {
            if (node == root) {
                continue;
            }
            out.append(node.id + 1).append(' ').append(node.parent.id + 1).println();
        }
    }

    public Node toDepth(Node root, int d) {
        if (d == 0) {
            return root;
        }
        int log = Log2.floorLog(d);
        return toDepth(root.jump[log], d - (1 << log));
    }

    public void setFather(Node a, Node p) {
        a.parent = p;
        a.jump[0] = p;
        for (int i = 0; a.jump[i] != null; i++) {
            a.jump[i + 1] = a.jump[i].jump[i];
        }
    }

    boolean valid = true;

    public void build(Node root, Node node, Node child) {
        if (node.visited) {
            return;
        }
        node.visited = true;
        int depth = root.dist[node.id] - 1;
        int distToChild = root.dist[child.id] - depth;

        for (IntegerIterator iterator = child.heights.iterator(distToChild); iterator.hasNext(); ) {
            int next = iterator.next();
            if (root.dist[next] == depth) {
                node.parent = nodes[next];
                build(root, node.parent, child);
                setFather(node, node.parent);
                return;
            }
        }
    }

    public int dist(int a, int b) {
        int c = lca.lca(a, b);
        return nodes[a].depth + nodes[b].depth - 2 * nodes[c].depth;
    }

    LcaOnTree lca;
    Node[] nodes;
}

class Node {
    int id;
    Node parent;
    int[] dist;
    IntegerMultiWayStack heights;
    int depth;
    boolean visited;
    Node[] jump = new Node[20];
    Node chain = this;
    Node cand = null;


    @Override
    public String toString() {
        return "" + id;
    }
}