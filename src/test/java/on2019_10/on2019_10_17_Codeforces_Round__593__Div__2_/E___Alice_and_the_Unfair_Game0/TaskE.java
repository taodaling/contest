package on2019_10.on2019_10_17_Codeforces_Round__593__Div__2_.E___Alice_and_the_Unfair_Game0;





import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] seq = new int[m];
        for (int i = 0; i < m; i++) {
            seq[i] = in.readInt() - 1;
        }
        if(n == 1 && m > 0){
            out.println(0);
            return;
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n; i++) {
            nodes[i].rewind();
        }

        Splay root = Splay.NIL;
        for (int i = 0; i < n; i++) {
            Splay s = new Splay();
            s.key = i;
            s.node = nodes[i];
            s.pushUp();
            root = Splay.add(root, s);
        }

        for (int i = 0; i < m; i++) {
            root = Splay.selectKeyAsRoot(root, seq[i] + 1);
            if (root.key != seq[i] + 1) {
                root.setFix(-1);
                continue;
            }
            root.left.setFix(-1);
            Splay r = Splay.splitRight(root);
            r = Splay.selectMinAsRoot(r);
            r.setFix(-1);
            if (r.key != root.key) {
                root.setRight(r);
                continue;
            }
            Node.merge(r.node, root.node);
            Splay l = Splay.splitLeft(root);
            r.pushDown();
            r.setLeft(l);
            root = r;
        }

        dfs(root);
        for(int i = 0; i < n; i++){
            nodes[i].left = nodes[i].find().place;
        }

        for(int i = 0; i < n; i++){
            nodes[i].rewind();
        }

        root = Splay.NIL;
        for (int i = 0; i < n; i++) {
            Splay s = new Splay();
            s.key = i;
            s.node = nodes[i];
            s.pushUp();
            root = Splay.add(root, s);
        }

        for (int i = 0; i < m; i++) {
            root = Splay.selectKeyAsRoot(root, seq[i] - 1);
            if (root.key != seq[i] - 1) {
                root.setFix(+1);
                continue;
            }
            root.right.setFix(+1);
            Splay r = Splay.splitLeft(root);
            r = Splay.selectMaxAsRoot(r);
            r.setFix(+1);
            if (r.key != root.key) {
                root.setLeft(r);
                continue;
            }
            Node.merge(r.node, root.node);
            Splay l = Splay.splitRight(root);
            r.pushDown();
            r.setRight(l);
            root = r;
        }

        dfs(root);
        for(int i = 0; i < n; i++){
            nodes[i].right = nodes[i].find().place;
        }

        long ans = 0;
        for(int i = 0; i < n; i++){
            int l = Math.max(0, nodes[i].left - 1);
            int r = Math.min(n - 1, nodes[i].right + 1);
            int range = r - l + 1;
            range = Math.max(0, range);
            ans += range;
        }

        out.println(ans);
    }

    public void dfs(Splay root){
        if(root == Splay.NIL){
            return;
        }
        root.pushDown();
        root.node.find().place = root.key;
        dfs(root.left);
        dfs(root.right);
    }
}

class Node {
    Node p;
    int rank;
    int place;
    int left;
    int right;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }

    public void rewind() {
        p = this;
        rank = 0;
    }

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
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
        NIL.key = -(int)1e9;
    }

    Splay left = NIL;
    Splay right = NIL;
    Splay father = NIL;
    int size = 1;
    int key;
    int fix;
    Node node;

    public void setFix(int f) {
        if (this == NIL) {
            return;
        }
        key += f;
        fix += f;
    }

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
        size = left.size + right.size + 1;
    }

    public void pushDown() {
        if(this == NIL){
            return;
        }
        if (fix != 0) {
            left.setFix(fix);
            right.setFix(fix);
            fix = 0;
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
        builder.append(root.key).append(',');
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

    public static Splay[] split(Splay root, int key) {
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append(key).append(":");
        toString(cloneTree(this), builder);
        return builder.toString();
    }
}
