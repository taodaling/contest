package on2020_05.on2020_05_07_Codeforces___Codeforces_Round__639__Div__1_.E__Train_Tracks;



import jdk.nashorn.internal.ir.LiteralNode;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;
import template.primitve.generated.datastructure.LongIterator;
import template.primitve.generated.datastructure.LongMultiWayDeque;
import template.primitve.generated.datastructure.LongMultiWayStack;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ETrainTracks {
    {
        LCTNode.dq.clear();
        LCTNode.time = -1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            LCTNode a = nodes[in.readInt() - 1];
            LCTNode b = nodes[in.readInt() - 1];
            Edge edge = new Edge();
            edge.a = a;
            edge.b = b;
            edge.d = in.readInt();
            a.next.add(edge);
            b.next.add(edge);
        }

        dfs(nodes[0], null, 0);
        LCTNode.time = -1;
        for (int i = 0; i < m; i++) {
            LCTNode to = nodes[in.readInt() - 1];
            long time = in.readLong();
            LCTNode.time = time;
            LCTNode.access(to);
            LCTNode.splay(nodes[0]);
            nodes[0].lastTrain = time;
        }


        PriorityQueue<LCTNode> wait = new PriorityQueue<>(n, (a, b) -> Long.compare(a.begin, b.begin));
        PriorityQueue<LCTNode> pq = new PriorityQueue<>(n, (a, b) -> Long.compare(a.front, b.front));
        for (LCTNode node : nodes) {
            if (LCTNode.dq.isEmpty(node.id)) {
                continue;
            }

            node.iterator = LCTNode.dq.iterator(node.id);
            node.distIterator = LCTNode.distDq.iterator(node.id);
            node.begin = 1;
            node.front = node.iterator.next();
            pq.add(node);
        }

        long inf = (long) 1e18;
        long time = inf;
        for (long i = 1; pq.size() + wait.size() > 0; i++) {
            if (pq.isEmpty()) {
                i = Math.max(i, wait.peek().begin);
            }

            while (!wait.isEmpty() && wait.peek().begin <= i) {
                pq.add(wait.remove());
            }

            LCTNode head = pq.remove();
            if (head.front < i) {
                time = head.front;
                break;
            }

            if (head.iterator.hasNext()) {
                head.begin = head.front + head.distIterator.next();
                head.front = head.iterator.next();
                wait.add(head);
            }
        }


        int req = 0;
        out.append(time == inf ? -1 : time).append(' ');
        for (int i = 0; i < n; i++) {
            for (LongIterator iterator = LCTNode.dq.iterator(i); iterator.hasNext(); ) {
                long next = iterator.next();
                if (next < time) {
                    req++;
                }
            }
        }

        out.println(req);
    }

    public void dfs(LCTNode root, LCTNode p, long depth) {
        root.depth = depth;
        for (Edge e : root.next) {
            LCTNode node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, depth + e.d);
            root.pushDown();
            root.right.father = LCTNode.NIL;
            root.right.treeFather = root;
            root.setRight(node);
            root.pushUp();
        }
    }
}


class Edge {
    LCTNode a;
    LCTNode b;
    long d;

    LCTNode other(LCTNode x) {
        return x == a ? b : a;
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
        NIL.id = -1;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    long front;
    long begin;
    LongIterator iterator;
    LongIterator distIterator;

    static long inf = (long) 1e18;
    long lastTrain = -inf;

    List<Edge> next = new ArrayList<>();
    long depth;
    static LongMultiWayDeque dq = new LongMultiWayDeque(100000, 4000000);
    static LongMultiWayDeque beginDq = new LongMultiWayDeque(100000, 4000000);
    static long time = -1;

    public static void access(LCTNode x) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.setRight(last);
            x.pushUp();

            LCTNode right = x.right;
            if (right != NIL) {
                right.pushDown();
                while (right.left != NIL) {
                    right = right.left;
                    right.pushDown();
                }
            }

            last = x;
            x = x.treeFather;
            if (time != -1 && x != NIL) {
                dq.addLast(x.id, time + x.depth);
            }
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
        splay(x);
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
        if (this == NIL) {
            return;
        }
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        left.lastTrain = lastTrain;
        right.lastTrain = lastTrain;
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
        if (this == NIL) {
            return;
        }
    }
}
