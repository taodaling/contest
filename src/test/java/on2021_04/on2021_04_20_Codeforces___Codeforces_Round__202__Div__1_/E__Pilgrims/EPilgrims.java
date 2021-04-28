package on2021_04.on2021_04_20_Codeforces___Codeforces_Round__202__Div__1_.E__Pilgrims;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EPilgrims {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            nodes[in.ri() - 1].special = true;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.c = in.ri();
            a.adj.add(e);
            b.adj.add(e);
        }

        Node first = Arrays.stream(nodes).filter(x -> x.special).findFirst().orElse(null);
        dfsForSize(first, null);

        dfsForDepth(first, null, 0);
        Node end1 = findMax(nodes);
        dfsForDepth(end1, null, 0);
        Node end2 = findMax(nodes);
        int diameter = end2.depth;
        dq = new ArrayDeque<>(n);
        search(end1, null, end2);
        Node left = null;
        Node right = null;
        for (Node node : dq) {
            if (node.depth * 2 <= diameter) {
                left = node;
            }
            if (right == null && node.depth * 2 >= diameter) {
                right = node;
            }
        }
        if (left != right) {
            Node a = left;
            Node b = right;
            dfsForDepth(a, b, 0);
            dfsForSize(a, b);
            dfsForDepth(b, a, 0);
            dfsForSize(b, a);
            calcReduce(a, b, a.maxDepth, a.maxDepthCnt, b.size);
            calcReduce(b, a, b.maxDepth, b.maxDepthCnt, a.size);
        } else {
            Node center = left;
            dfsForDepth(center, null, 0);
            dfsForSize(center, null);
            if (!center.special) {
                center.reduce = m;
            }
            List<Node> subtree = new ArrayList<>(n);
            for (Edge e : center.adj) {
                Node node = e.other(center);
                if (node.deleted) {
                    continue;
                }
                if (node.maxDepth == center.maxDepth) {
                    subtree.add(node);
                }
            }
            for (Edge e : center.adj) {
                Node node = e.other(center);
                if (node.deleted) {
                    continue;
                }
                int extra = 0;
                if (subtree.size() == 2 && node.maxDepth == center.maxDepth) {
                    for(Node other : subtree){
                        if(other == node){
                            continue;
                        }
                        extra += other.size;
                    }
                }
                calcReduce(node, center, node.maxDepth, node.maxDepthCnt, extra);
            }
        }

        int way = 0;
        int best = -1;
        for (Node node : nodes) {
            if (node.deleted) {
                continue;
            }
            if (node.reduce > best) {
                best = node.reduce;
                way = 0;
            }
            if (node.reduce == best) {
                way++;
            }
        }
        if(best == 0){
            way = n - m;
        }
        out.println(best).println(way);
    }

    public Node findMax(Node[] nodes) {
        Node best = null;
        for (Node node : nodes) {
            if (node.deleted) {
                continue;
            }
            if (best == null || best.depth < node.depth) {
                best = node;
            }
        }
        return best;
    }

    public void dfsForSize(Node root, Node p) {
        root.size = root.special ? 1 : 0;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p || node.deleted) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
        if (root.size == 0) {
            root.deleted = true;
        }
    }

    public void dfsForDepth(Node root, Node p, int d) {
        root.depth = d;
        root.maxDepth = root.depth;
        root.maxDepthCnt = 1;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p || node.deleted) {
                continue;
            }
            dfsForDepth(node, root, d + e.c);
            if (node.maxDepth > root.maxDepth) {
                root.maxDepth = node.maxDepth;
                root.maxDepthCnt = 0;
            }
            if (node.maxDepth == root.maxDepth) {
                root.maxDepthCnt += node.maxDepthCnt;
            }
        }
    }

    public void calcReduce(Node root, Node p, int deepest, int total, int extra) {
        if (!root.special) {
            root.reduce += root.size;
            if (deepest == root.maxDepth && root.maxDepthCnt == total) {
                root.reduce += extra;
            }
        }
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p || node.deleted) {
                continue;
            }
            calcReduce(node, root, deepest, total, extra);
        }
    }

    Deque<Node> dq;

    public boolean search(Node root, Node p, Node target) {
        dq.addLast(root);
        if (root == target) {
            return true;
        }
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p || node.deleted) {
                continue;
            }
            if (search(node, root, target)) {
                return true;
            }
        }
        dq.removeLast();
        return false;
    }
}

class Edge {
    Node a;
    Node b;
    int c;

    Node other(Node x) {
        return a == x ? b : a;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    boolean special;
    boolean deleted;
    int size;
    int depth;

    int maxDepth;
    int maxDepthCnt;

    int reduce;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
