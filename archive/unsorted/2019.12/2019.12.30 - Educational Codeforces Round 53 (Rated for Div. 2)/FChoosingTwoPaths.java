package contest;

import template.graph.ForestDiameter;
import template.graph.TreeDiameter;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.MultiWayIntegerDeque;
import template.primitve.generated.MultiWayIntegerStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class FChoosingTwoPaths {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        int[][] edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
            Node a = nodes[edges[i][0]];
            Node b = nodes[edges[i][1]];
            a.next.add(b);
            b.next.add(a);
        }

        Node root = null;
        for (Node node : nodes) {
            if (node.next.size() >= 3) {
                root = node;
                break;
            }
        }
        dfsForDepth(root, null, 0);

        for (Node node : nodes) {
            if (node.next.size() == 1) {
                upward(node);
            }
        }

        MultiWayIntegerStack edgeStack = new MultiWayIntegerStack(n, n * 2);
        for (int[] e : edges) {
            if (nodes[e[0]].removed || nodes[e[1]].removed) {
                continue;
            }
            edgeStack.addLast(e[0], e[1]);
            edgeStack.addLast(e[1], e[0]);
        }
        ForestDiameter td = new ForestDiameter(edgeStack, n);
        if (td.getCenters(root.id).size() == 1) {
            Node center = nodes[td.getCenters(root.id).get(0)];
            int sd = td.getDiameter(root.id) / 2;
            List<NodePair> pairs = new ArrayList<>(center.next.size());
            for (Node node : center.next) {
                pairs.add(findLargestSum(node, center, 1, sd));
            }
            pairs.sort(this::compare);
            NodePair a = pairs.get(pairs.size() - 1);
            NodePair b = pairs.get(pairs.size() - 2);
            answer(out, a, b);
            return;
        }

        Node c1 = nodes[td.getCenters(root.id).get(0)];
        Node c2 = nodes[td.getCenters(root.id).get(1)];
        int sd = (td.getDiameter(root.id) - 1) / 2;
        NodePair a = findLargestSum(c1, c2, 0, sd);
        NodePair b = findLargestSum(c2, c1, 0, sd);
        answer(out, a, b);
    }

    public void answer(FastOutput out, NodePair a, NodePair b) {
        out.append(a.a.id + 1).append(' ').append(b.a.id + 1).println();
        out.append(a.b.id + 1).append(' ').append(b.b.id + 1).println();
    }

    public NodePair max(NodePair a, NodePair b) {
        return compare(a, b) >= 0 ? a : b;
    }

    public int compare(NodePair a, NodePair b) {
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return a.len - b.len;
    }

    public NodePair findLargestSum(Node root, Node p, int depth, int searchDepth) {
        if(root.removed){
            return null;
        }

        NodePair ans = null;
        root.depth = depth;

        if (depth == searchDepth) {
            List<Node> nodes = new ArrayList<>(root.next.size());
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                nodes.add(findDepthest(node, root, depth + 1));
            }
            nodes.sort((a, b) -> -(a.depth - b.depth));
            NodePair pair = new NodePair();
            pair.a = nodes.get(0);
            pair.b = nodes.get(1);
            pair.len = pair.a.depth + pair.b.depth - 2 * depth;
            return pair;
        }

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            ans = max(ans, findLargestSum(node, root, depth + 1, searchDepth));
        }
        return ans;
    }

    public Node findDepthest(Node root, Node p, int depth) {
        root.depth = depth;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            return findDepthest(node, root, depth + 1);
        }
        return root;
    }

    public void upward(Node root) {
        if (root == null || root.removed || root.next.size() >= 3) {
            return;
        }
        root.removed = true;
        upward(root.p);
    }

    public void dfsForDepth(Node root, Node p, int depth) {
        root.depth = depth;
        root.p = p;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root, depth + 1);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    boolean removed;
    int id;
    int depth;
    Node p;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

class NodePair {
    Node a;
    Node b;
    int len;
}