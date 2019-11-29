package on2019_11.on2019_11_29_Educational_Codeforces_Round_77__Rated_for_Div__2_.F___Colored_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

import java.util.*;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].l = in.readInt();
            nodes[i].r = in.readInt();
            nodes[i].g = nodes[i].r - nodes[i].l + 1;
            nodes[i].invG = pow.inverse(nodes[i].g);
            nodes[i].depth = 0;
            nodes[i].rep = new LCTNode();
            nodes[i].rep.id = i;
            nodes[i].rep.pushUp();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
            LCTNode.join(a.rep, b.rep);
        }

        int ans = 0;
        dfs(nodes[0], null);
        Node[] nodeSortByL = nodes.clone();
        Node[] nodeSortByR = nodes.clone();
        Arrays.sort(nodeSortByL, (a, b) -> a.l - b.l);
        Arrays.sort(nodeSortByR, (a, b) -> a.r - b.r);

        Deque<Node> dequeByL = new ArrayDeque<>(Arrays.asList(nodeSortByL));
        Deque<Node> dequeByR = new ArrayDeque<>(Arrays.asList(nodeSortByR));
        int p = 1;
        for (int i = 0; i < n; i++) {
            p = mod.mul(p, nodes[i].g);
        }
        int stateL = 0;
        int stateR = 0;
        LCTNode.makeRoot(nodes[0].rep);
        int allInvG = 0;

        for (int i = 1; i <= 100000; i++) {
            while (!dequeByL.isEmpty() && dequeByL.peekFirst().l <= i) {
                //add
                Node head = dequeByL.removeFirst();
                int x = mod.mul(head.depth, head.invG);
                stateL = mod.plus(stateL, x);

                LCTNode.access(head.rep);
                LCTNode.splay(head.rep);
                stateR = mod.plus(stateR, mod.mul(head.invG, head.rep.sum));
                head.rep.update(head.invG);
                stateR = mod.plus(stateR, mod.mul(head.invG, head.rep.sum));

                allInvG = mod.plus(allInvG, head.invG);
            }
            while (!dequeByR.isEmpty() && dequeByR.peekFirst().r < i) {
                //remove
                Node head = dequeByR.removeFirst();

                int x = mod.mul(head.depth, head.invG);
                stateL = mod.subtract(stateL, x);

                LCTNode.access(head.rep);
                LCTNode.splay(head.rep);
                stateR = mod.subtract(stateR, mod.mul(head.invG, head.rep.sum));
                head.rep.update(-head.invG);
                stateR = mod.subtract(stateR, mod.mul(head.invG, head.rep.sum));

                allInvG = mod.subtract(allInvG, head.invG);
            }

            int state = mod.subtract(mod.mul(stateL, allInvG), stateR);
            int plus = mod.mul(state, p);
            ans = mod.plus(ans, plus);
        }

        out.println(ans);
    }


    public void dfs(Node root, Node p) {
        if (p == null) {
            root.depth = 1;
        } else {
            root.depth = p.depth + 1;
        }
        root.next.remove(p);
        for (Node node : root.next) {
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int depth;
    int g;
    int l;
    int r;
    int invG;
    LCTNode rep;
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();
    public static final Modular mod = new Modular(1e9 + 7);

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
    int sum;
    int val;
    int size;
    int dirty;

    public void update(int x) {
        if (this == NIL) {
            return;
        }
        sum = mod.valueOf((long) size * x + sum);
        dirty = mod.plus(dirty, x);
        val = mod.plus(val, x);
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

        if (dirty != 0) {
            left.update(dirty);
            right.update(dirty);
            dirty = 0;
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
        if (this == NIL) {
            return;
        }
        size = left.size + right.size + 1;
        sum = mod.valueOf((long) left.sum + right.sum + val);
    }
}
