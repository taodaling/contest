package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DirectedMST {
    long inf = Long.MAX_VALUE;
    int now;
    Node top;

    private static class Edge {
        Node src;
        Node dst;
        long weight;
        long fixedWeight;
        int id;

        @Override
        public String toString() {
            return "(" + src + "," + dst + ")[" + weight + "]";
        }
    }

    private static class Node {
        int id = -1;
        List<Edge> inEdges = new ArrayList<>();
        LeftistTree queue = LeftistTree.NIL;
        Node parent;
        Edge outEdge;
        Node outNode;
        int visited;


        Node p = this;
        int circleRank;

        Node currentLevel = this;

        Node find() {
            return p.p == p ? p : (p = p.find());
        }

        static void merge(Node a, Node b) {
            a = a.find();
            b = b.find();
            if (a == b) {
                return;
            }
            if (a.circleRank == b.circleRank) {
                a.circleRank++;
            }
            if (a.circleRank > b.circleRank) {
                b.p = a;
            } else {
                a.p = b;
            }
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }

    /**
     * <p>Find directed minimum spanning tree whose root is rootId. If it doesn't exist, there will be -1 in result, check it please.</p>
     *
     * @param rootId
     * @param result
     */
    public void dismantle(int rootId, IntegerArrayList result) {
        if (nodes.length == 1) {
            return;
        }
        now++;
        Node root = nodes[rootId];
        dismantle0(root, result);
    }

    private void dismantle0(Node root, IntegerArrayList result) {
        if (root == top || root.visited == now) {
            return;
        }
        root.visited = now;
        Node trace = root;
        while (true) {
            Node front = trace.outNode;
            Node frontRoot = trace.outEdge.dst;
            if (front == root) {
                break;
            }
            result.add(trace.outEdge.id);
            front.visited = now;
            dismantle0(frontRoot, result);
            trace = front;
        }
        dismantle0(root.parent, result);
    }

    public void contract() {
        now++;
        Deque<LeftistTree> deque = new ArrayDeque<>();
        for (Node node : nodes) {
            for (Edge edge : node.inEdges) {
                edge.fixedWeight = edge.weight;
                deque.addLast(new LeftistTree(edge));
            }
            node.queue = LeftistTree.createFromDeque(deque);
        }

        Deque<Node> stack = new ArrayDeque<>();
        stack.addLast(nodes[0]);
        nodes[0].visited = now;
        int remain = nodes.length;
        while (remain > 1) {
            Node tail = stack.peekLast().currentLevel;
            Edge minInEdge = null;
            while (true) {
                minInEdge = tail.queue.peek();
                tail.queue = LeftistTree.pop(tail.queue);
                //self loop
                if (minInEdge.src.find() != minInEdge.dst.find()) {
                    break;
                }
            }

            Node x = minInEdge.src.find().currentLevel;
            //No loop
            if (x.visited != now) {
                x.visited = now;
                x.outEdge = minInEdge;
                stack.addLast(x);
                continue;
            }
            //Find loop, merge them together
            Node p = new Node();
            p.visited = now;
            p.outEdge = x.outEdge;
            x.outEdge = minInEdge;
            Node last = x;
            while (true) {
                Node t = stack.removeLast();
                t.parent = p;
                last.outNode = t;
                t.queue.modify(-last.outEdge.fixedWeight);
                p.queue = LeftistTree.merge(t.queue, p.queue);
                Node.merge(p, t);
                last = t;
                remain--;
                if (t == x) {
                    break;
                }
            }
            p.find().currentLevel = p;
            stack.addLast(p);
            remain++;
        }
        top = stack.removeLast();
    }

    private Node[] nodes;

    public void addEdge(int id, int s, int t, long weight) {
        Edge edge = new Edge();
        edge.id = id;
        edge.src = nodes[s];
        edge.dst = nodes[t];
        edge.weight = weight;
        edge.dst.inEdges.add(edge);
    }

    public DirectedMST(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            addEdge(-1, i, (i + 1) % n, inf);
        }
    }

    private static class LeftistTree {
        public static final LeftistTree NIL = new LeftistTree(null);

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.dist = -1;
        }

        LeftistTree left = NIL;
        LeftistTree right = NIL;
        int dist;
        DirectedMST.Edge key;
        long mod;

        public void pushDown() {
            if (mod != 0) {
                left.modify(mod);
                right.modify(mod);
                mod = 0;
            }
        }

        public void modify(long k) {
            if (this == NIL) {
                return;
            }
            key.fixedWeight += k;
            mod += k;
        }

        public LeftistTree(DirectedMST.Edge key) {
            this.key = key;
        }

        public static LeftistTree createFromDeque(Deque<LeftistTree> deque) {
            while (deque.size() > 1) {
                deque.addLast(merge(deque.removeFirst(), deque.removeFirst()));
            }
            return deque.removeLast();
        }

        public static LeftistTree merge(LeftistTree a, LeftistTree b) {
            if (a == NIL) {
                return b;
            } else if (b == NIL) {
                return a;
            }
            a.pushDown();
            b.pushDown();
            if (a.key.fixedWeight > b.key.fixedWeight) {
                LeftistTree tmp = a;
                a = b;
                b = tmp;
            }
            a.right = merge(a.right, b);
            if (a.left.dist < a.right.dist) {
                LeftistTree tmp = a.left;
                a.left = a.right;
                a.right = tmp;
            }
            a.dist = a.right.dist + 1;
            return a;
        }

        public DirectedMST.Edge peek() {
            return key;
        }

        public static LeftistTree pop(LeftistTree root) {
            root.pushDown();
            return merge(root.left, root.right);
        }
    }
}