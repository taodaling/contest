package template.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class DirectMinSpanningTree {
    long inf = Long.MAX_VALUE;
    int now;
    Node top;

    public static class Edge {
        Node src;
        Node dst;
        long weight;
        long fixedWeight;

        @Override
        public String toString() {
            return "(" + src + "," + dst + ")[" + weight + "]";
        }
    }

    public static class Node {
        int id = -1;
        List<Edge> inEdges = new ArrayList<>(2);
        LeftSideTree queue = LeftSideTree.NIL;
        Node parent;
        Edge outEdge;
        Node outNode;
        int visited;


        Node circleP = this;
        int circleRank;

        Node currentLevel = this;

        Node find() {
            return circleP.circleP == circleP ? circleP : (circleP = circleP.find());
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
                b.circleP = a;
            } else {
                a.circleP = b;
            }
        }

        @Override
        public String toString() {
            return "" + id;
        }
    }

    public List<Edge> dismantle(int rootId) {
        if (nodes.length == 1) {
            return Collections.emptyList();
        }
        now++;
        Node root = nodes[rootId];
        List<Edge> result = new ArrayList<>();
        dismantle0(root, result);
        return result;
    }

    private void dismantle0(Node root, List<Edge> result) {
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
            result.add(trace.outEdge);
            front.visited = now;
            dismantle0(frontRoot, result);
            trace = front;
        }
        dismantle0(root.parent, result);
    }

    public void contract() {
        now++;
        Deque<LeftSideTree> deque = new ArrayDeque<>();
        for (Node node : nodes) {
            for (Edge edge : node.inEdges) {
                edge.fixedWeight = edge.weight;
                deque.addLast(new LeftSideTree(edge));
            }
            node.queue = LeftSideTree.createFromDeque(deque);
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
                tail.queue = LeftSideTree.pop(tail.queue);
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
                p.queue = LeftSideTree.merge(t.queue, p.queue);
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

    public void addEdge(int s, int t, long weight) {
        Edge edge = new Edge();
        edge.src = nodes[s];
        edge.dst = nodes[t];
        edge.weight = weight;
        edge.dst.inEdges.add(edge);
    }

    public DirectMinSpanningTree(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            addEdge(i, (i + 1) % n, inf);
        }
    }

    private static class LeftSideTree {
        public static final LeftSideTree NIL = new LeftSideTree(null);

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.dist = -1;
        }

        LeftSideTree left = NIL;
        LeftSideTree right = NIL;
        int dist;
        DirectMinSpanningTree.Edge key;
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

        public LeftSideTree(DirectMinSpanningTree.Edge key) {
            this.key = key;
        }

        public static LeftSideTree createFromDeque(Deque<LeftSideTree> deque) {
            while (deque.size() > 1) {
                deque.addLast(merge(deque.removeFirst(), deque.removeFirst()));
            }
            return deque.removeLast();
        }

        public static LeftSideTree merge(LeftSideTree a, LeftSideTree b) {
            if (a == NIL) {
                return b;
            } else if (b == NIL) {
                return a;
            }
            a.pushDown();
            b.pushDown();
            if (a.key.fixedWeight > b.key.fixedWeight) {
                LeftSideTree tmp = a;
                a = b;
                b = tmp;
            }
            a.right = merge(a.right, b);
            if (a.left.dist < a.right.dist) {
                LeftSideTree tmp = a.left;
                a.left = a.right;
                a.right = tmp;
            }
            a.dist = a.right.dist + 1;
            return a;
        }

        public DirectMinSpanningTree.Edge peek() {
            return key;
        }

        public static LeftSideTree pop(LeftSideTree root) {
            root.pushDown();
            return merge(root.left, root.right);
        }
    }
}