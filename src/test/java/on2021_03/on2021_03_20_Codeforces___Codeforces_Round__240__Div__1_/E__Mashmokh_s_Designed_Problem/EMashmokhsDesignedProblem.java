package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__240__Div__1_.E__Mashmokh_s_Designed_Problem;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.lang.invoke.SerializedLambda;
import java.util.List;
import java.util.function.IntFunction;

public class EMashmokhsDesignedProblem {
    List<Integer>[] g;
    Splay[] open;
    Splay[] close;

    private Splay newNode(int root, int d) {
        Splay ans = new Splay();
        ans.key = d;
        ans.id = root;
        ans.pushUp();
        return ans;
    }

    public Splay dfs(int root, int p, int d) {
        open[root] = newNode(root, d);
        close[root] = newNode(root, d);
        Splay now = open[root];
        SequenceUtils.reverse(g[root]);
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            now = Splay.merge(now, dfs(node, root, d + 1));
        }
        now = Splay.merge(now, close[root]);
        return now;
    }

    public Splay findAtMostK(Splay root, int k) {
        Splay last = root;
        while (root != Splay.NIL) {
            last = root;
            root.pushDown();
            if (root.left.minKey <= k) {
                root = root.left;
            } else if (root.key <= k) {
                break;
            } else {
                root = root.right;
            }
        }
        Splay.splay(last);
        return last;
    }

    public Splay findAtLeastK(Splay root, int k) {
        Splay last = root;
        while (root != Splay.NIL) {
            last = root;
            root.pushDown();
            if (root.left.maxKey >= k) {
                root = root.left;
            } else if (root.key >= k) {
                break;
            } else {
                root = root.right;
            }
        }
        Splay.splay(last);
        return last;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        g = Graph.createGraph(n);
        open = new Splay[n];
        close = new Splay[n];
        for (int i = 0; i < n; i++) {
            int l = in.ri();
            for (int j = 0; j < l; j++) {
                g[i].add(in.ri() - 1);
            }
        }
        Splay tree = dfs(0, -1, 0);
        for (int i = 0; i < m; i++) {
            debug.debug("i", i);
            Splay.splay(tree);
            debug.debug("tree", tree);
            int t = in.ri();
            if (t == 1) {
                int v = in.ri() - 1;
                int u = in.ri() - 1;
                int vo = Splay.rank(open[v]);
                int uo = Splay.rank(open[u]);
                if (vo > uo) {
                    {
                        int tmp = vo;
                        vo = uo;
                        uo = tmp;
                    }
                    {
                        int tmp = v;
                        v = u;
                        u = tmp;
                    }
                }
                int vc = Splay.rank(close[v]);
                int uc = Splay.rank(close[u]);
                int lcaDepth = -1;
                if (vc >= uc) {
                    lcaDepth = open[v].key;
                } else {
                    assert vc < uo;
                    Splay.splay(open[v]);
                    Splay[] split = Splay.splitByRank(open[v], uo);
                    Splay[] split2 = Splay.splitByRank(open[v], vc);
                    lcaDepth = split2[1].minKey - 1;
                    Splay.merge(split2[0], split2[1]);
                    Splay.merge(split[0], split[1]);
                }
                int dist = open[u].key + open[v].key - 2 * lcaDepth;
                out.println(dist);
            } else if (t == 2) {
                int v = in.ri() - 1;
                Splay.splay(close[v]);
                int h = close[v].key - in.ri();
                Splay right = Splay.splitRight(close[v]);
                int ancestor = findAtMostK(right, h).id;
                Splay.merge(close[v], right);
                Splay.splay(open[v]);
                Splay.splitLeft(open[v]);
                Splay.splay(close[v]);
                Splay.splitRight(close[v]);
                close[v].modify((h + 1) - close[v].key);
                Splay.splay(open[ancestor]);
                Splay prev = Splay.splitRight(open[ancestor]);
                Splay.merge(open[ancestor], open[v]);
                Splay.merge(open[ancestor], prev);
                Splay.merge(open[ancestor], close[ancestor]);
            } else {
                int k = in.ri();
                Splay.splay(open[0]);
                Splay ans = findAtLeastK(open[0], k);
                out.println(ans.id + 1);
            }
        }
    }
}


class Splay implements Cloneable {
    public static final Splay NIL = new Splay();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.size = 0;
        NIL.maxKey = -1;
        NIL.minKey = (int) 1e9;
    }

    Splay left = NIL;
    Splay right = NIL;
    Splay father = NIL;

    int id;
    int size = 1;
    int key;
    int maxKey;
    int dirty;
    int minKey;

    public static void splay(Splay x) {
        if (x == NIL) {
            return;
        }
        Splay y, z;
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

    public static void zig(Splay x) {
        Splay y = x.father;
        Splay z = y.father;
        Splay b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(Splay x) {
        Splay y = x.father;
        Splay z = y.father;
        Splay b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public void setLeft(Splay x) {
        left = x;
        x.father = this;
    }

    public void setRight(Splay x) {
        right = x;
        x.father = this;
    }

    public void changeChild(Splay y, Splay x) {
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
        maxKey = Math.max(left.maxKey, right.maxKey);
        maxKey = Math.max(key, maxKey);
        minKey = Math.min(left.minKey, right.minKey);
        minKey = Math.min(key, minKey);
    }

    public void modify(int x) {
        if (this == NIL) {
            return;
        }
        key += x;
        maxKey += x;
        minKey += x;
        dirty += x;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }


    public static int toArray(Splay root, int[] data, int offset) {
        if (root == NIL) {
            return offset;
        }
        offset = toArray(root.left, data, offset);
        data[offset++] = root.key;
        offset = toArray(root.right, data, offset);
        return offset;
    }

    public static void toString(Splay root, StringBuilder builder) {
        if (root == NIL) {
            return;
        }
        root.pushDown();
        toString(root.left, builder);
        builder.append("[").append(root.id).append("|").append(root.key).append("]").append(',');
        toString(root.right, builder);
    }

    public Splay clone() {
        try {
            return (Splay) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Splay cloneTree(Splay splay) {
        if (splay == NIL) {
            return NIL;
        }
        splay = splay.clone();
        splay.left = cloneTree(splay.left);
        splay.right = cloneTree(splay.right);
        return splay;
    }

    public static Splay add(Splay root, Splay node) {
        if (root == NIL) {
            return node;
        }
        Splay p = root;
        while (root != NIL) {
            p = root;
            root.pushDown();
            if (root.key < node.key) {
                root = root.right;
            } else {
                root = root.left;
            }
        }

        if (p.key < node.key) {
            p.setRight(node);
        } else {
            p.setLeft(node);
        }
        p.pushUp();
        splay(node);
        return node;
    }

    /**
     * Make the node with the minimum key as the root of tree
     */
    public static Splay selectMinAsRoot(Splay root) {
        if (root == NIL) {
            return root;
        }
        root.pushDown();
        while (root.left != NIL) {
            root = root.left;
            root.pushDown();
        }
        splay(root);
        return root;
    }

    /**
     * Make the node with the maximum key as the root of tree
     */
    public static Splay selectMaxAsRoot(Splay root) {
        if (root == NIL) {
            return root;
        }
        root.pushDown();
        while (root.right != NIL) {
            root = root.right;
            root.pushDown();
        }
        splay(root);
        return root;
    }

    /**
     * delete root of tree, then merge remain nodes into a new tree, and return the new root
     */
    public static Splay deleteRoot(Splay root) {
        root.pushDown();
        Splay left = splitLeft(root);
        Splay right = splitRight(root);
        return merge(left, right);
    }

    /**
     * detach the left subtree from root and return the root of left subtree
     */
    public static Splay splitLeft(Splay root) {
        root.pushDown();
        Splay left = root.left;
        left.father = NIL;
        left.pushDown();
        root.setLeft(NIL);
        root.pushUp();
        return left;
    }

    /**
     * detach the right subtree from root and return the root of right subtree
     */
    public static Splay splitRight(Splay root) {
        root.pushDown();
        Splay right = root.right;
        right.father = NIL;
        right.pushDown();
        root.setRight(NIL);
        root.pushUp();
        return right;
    }


    public static Splay merge(Splay a, Splay b) {
        if (a == NIL) {
            return b;
        }
        if (b == NIL) {
            return a;
        }
        splay(a);
        splay(b);
        a = selectMaxAsRoot(a);
        a.setRight(b);
        a.pushUp();
        return a;
    }

    public static Splay selectKthAsRoot(Splay root, int k) {
        if (root == NIL) {
            return NIL;
        }
        Splay trace = root;
        Splay father = NIL;
        while (trace != NIL) {
            father = trace;
            trace.pushDown();
            if (trace.left.size >= k) {
                trace = trace.left;
            } else {
                k -= trace.left.size + 1;
                if (k == 0) {
                    break;
                } else {
                    trace = trace.right;
                }
            }
        }
        splay(father);
        return father;
    }

    /**
     * Find the left most node with key k, make it as root(Or the largest value less than k if k not exists)
     */
    public static Splay selectKeyAsRoot(Splay root, int k) {
        if (root == NIL) {
            return NIL;
        }
        Splay trace = root;
        Splay father = NIL;
        Splay find = NIL;
        while (trace != NIL) {
            father = trace;
            trace.pushDown();
            if (trace.key > k) {
                trace = trace.left;
            } else {
                if (trace.key == k) {
                    find = trace;
                    trace = trace.left;
                } else {
                    trace = trace.right;
                }
            }
        }

        splay(father);
        if (find != NIL) {
            splay(find);
            return find;
        }
        return father;
    }

    public static Splay bruteForceMerge(Splay a, Splay b) {
        if (a == NIL) {
            return b;
        } else if (b == NIL) {
            return a;
        }
        if (a.size < b.size) {
            Splay tmp = a;
            a = b;
            b = tmp;
        }

        a = selectMaxAsRoot(a);
        int k = a.key;
        while (b != NIL) {
            b = selectMinAsRoot(b);
            if (b.key >= k) {
                break;
            }
            Splay kickedOut = b;
            b = deleteRoot(b);
            a = add(a, kickedOut);
        }
        return merge(a, b);
    }

    /**
     * Split the tree, and store key <= specified key in result[0]
     */
    public static Splay[] splitByKey(Splay root, int key) {
        if (root == NIL) {
            return new Splay[]{NIL, NIL};
        }
        Splay p = root;
        while (root != NIL) {
            p = root;
            root.pushDown();
            if (root.key > key) {
                root = root.left;
            } else {
                root = root.right;
            }
        }

        splay(p);
        if (p.key <= key) {
            return new Splay[]{p, splitRight(p)};
        } else {
            return new Splay[]{splitLeft(p), p};
        }
    }

    /**
     * Split the tree, and store rank <= specified key in result[0]
     */
    public static Splay[] splitByRank(Splay root, int rank) {
        root = Splay.selectKthAsRoot(root, rank);
        Splay[] ans = new Splay[2];
        ans[0] = root;
        ans[1] = Splay.splitRight(root);
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append("[").append(id).append("|").append(key).append("]").append(":");
        toString(cloneTree(this), builder);
        return builder.toString();
    }

    public static int rank(Splay root) {
        assert root != NIL;
        splay(root);
        return root.left.size + 1;
    }
}
