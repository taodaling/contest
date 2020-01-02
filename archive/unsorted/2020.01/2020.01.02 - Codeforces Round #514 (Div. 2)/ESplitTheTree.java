package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class ESplitTheTree {
    int l;
    long s;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        l = in.readInt();
        s = in.readLong();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].w = in.readInt();
            nodes[i].lct = new LCTNode();
            if(nodes[i].w > s){
                out.println(-1);
                return;
            }
        }

        for (int i = 2; i <= n; i++) {
            Node p = nodes[in.readInt()];
            nodes[i].next.add(p);
            p.next.add(nodes[i]);

            LCTNode.join(nodes[i].lct, p.lct);
        }

        dfs(nodes[1], null, 0);
        PriorityQueue<Node> pq = new PriorityQueue<>(n, (a, b) -> -(a.depth - b.depth));
        pq.addAll(Arrays.asList(nodes).subList(1, nodes.length));

        int cost = 0;
        while (!pq.isEmpty()) {
            Node head = pq.remove();
            LCTNode.splay(head.lct);
            if (head.lct.handled) {
                continue;
            }
            Node top = find(head);
            LCTNode.findRoute(head.lct, top.lct);
            LCTNode.splay(head.lct);
            head.lct.setHandled();
            cost++;
        }

        out.println(cost);
    }

    public Node find(Node root) {
        Node trace = root;
        for (int i = 20 - 1; i >= 0; i--) {
            if (trace.jumps[i] == null) {
                continue;
            }
            if (root.depth - trace.jumps[i].depth + 1 <= l &&
                    root.presum - trace.jumps[i].presum + trace.jumps[i].w <= s) {
                trace = trace.jumps[i];
            }
        }
        return trace;
    }

    public void dfs(Node root, Node p, int depth) {
        root.jumps[0] = p;
        for (int i = 0; root.jumps[i] != null; i++) {
            root.jumps[i + 1] = root.jumps[i].jumps[i];
        }
        root.depth = depth;
        root.presum = root.w;
        if (p != null) {
            root.presum += p.presum;
        }
        for (Node node : root.next) {
            if(node == p){
                continue;
            }
            dfs(node, root, depth + 1);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    Node[] jumps = new Node[20];
    int id;
    int w;
    long presum;
    int depth;
    LCTNode lct;

    @Override
    public String toString() {
        return "" + id;
    }
}


/**
 * Created by dalt on 2018/5/20.
 */
class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    boolean handled;
    boolean dirty;

    public void setHandled() {
        handled = true;
        dirty = true;
    }

    public static void access(LCTNode x) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.setRight(last);
            x.pushUp();

            last = x;
            x = x.treeFather;
        }
    }

    public static void makeRoot(LCTNode x) {
        access(x);
        splay(x);
        x.reverse();
    }

    public static void cut(LCTNode y, LCTNode x) {
        makeRoot(y);
        access(x);
        splay(y);
        y.right.treeFather = NIL;
        y.right.father = NIL;
        y.setRight(NIL);
        y.pushUp();
    }

    public static void join(LCTNode y, LCTNode x) {
        makeRoot(x);
        x.treeFather = y;
    }

    public static void findRoute(LCTNode x, LCTNode y) {
        makeRoot(y);
        access(x);
    }

    public static void splay(LCTNode x) {
        if (x == NIL) {
            return;
        }
        LCTNode y, z;
        while ((y = x.father) != NIL) {
            if ((z = y.father) == NIL) {
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    zig(x);
                } else {
                    zag(x);
                }
            } else {
                z.pushDown();
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    if (y == z.left) {
                        zig(y);
                        zig(x);
                    } else {
                        zig(x);
                        zag(x);
                    }
                } else {
                    if (y == z.left) {
                        zag(x);
                        zig(x);
                    } else {
                        zag(y);
                        zag(x);
                    }
                }
            }
        }

        x.pushDown();
        x.pushUp();
    }

    public static void zig(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static LCTNode findRoot(LCTNode x) {
        x.pushDown();
        while (x.left != NIL) {
            x = x.left;
            x.pushDown();
        }
        splay(x);
        return x;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    public void pushDown() {
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        if (dirty) {
            left.setHandled();
            right.setHandled();
            dirty = false;
        }

        left.treeFather = treeFather;
        right.treeFather = treeFather;
    }

    public void reverse() {
        reverse = !reverse;
    }

    public void setLeft(LCTNode x) {
        left = x;
        x.father = this;
    }

    public void setRight(LCTNode x) {
        right = x;
        x.father = this;
    }

    public void changeChild(LCTNode y, LCTNode x) {
        if (left == y) {
            setLeft(x);
        } else {
            setRight(x);
        }
    }

    public void pushUp() {
    }
}

