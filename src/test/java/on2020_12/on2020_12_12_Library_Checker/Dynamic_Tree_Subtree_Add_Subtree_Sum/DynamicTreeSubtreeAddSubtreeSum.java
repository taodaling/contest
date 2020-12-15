package on2020_12.on2020_12_12_Library_Checker.Dynamic_Tree_Subtree_Add_Subtree_Sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongObjectHashMap;

public class DynamicTreeSubtreeAddSubtreeSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        EulerTourTree ett = new EulerTourTree(n);
        for (int i = 0; i < n; i++) {
            ett.nodes[i].val = in.readInt();
            ett.nodes[i].pushUp();
        }
        for (int i = 0; i < n - 1; i++) {
            ett.link(in.readInt(), in.readInt());
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                ett.cut(in.readInt(), in.readInt());
                ett.link(in.readInt(), in.readInt());
            } else if (t == 1) {
                int r = in.readInt();
                int p = in.readInt();
                ett.cut(r, p);
                EulerTourTree.Splay.splay(ett.nodes[r]);
                ett.nodes[r].modify(in.readInt());
                ett.nodes[r].pushUp();
                ett.link(r, p);
            } else {
                int v = in.readInt();
                int p = in.readInt();
                ett.cut(v, p);
                EulerTourTree.Splay.splay(ett.nodes[v]);
                out.println(ett.nodes[v].sum);
                ett.link(v, p);
            }
        }
    }
}

class EulerTourTree {
    EulerTourTree.Splay[] nodes;
    LongObjectHashMap<EulerTourTree.Edge> map;

    public EulerTourTree(int n) {
        map = new LongObjectHashMap<>(n, true);
        nodes = new EulerTourTree.Splay[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = alloc(i);
            nodes[i].w = 1;
            nodes[i].pushUp();
        }
    }

    private EulerTourTree.Splay alloc(int id) {
        EulerTourTree.Splay splay = new Splay();//buffer.alloc();
        splay.id = id;
        return splay;
    }

    private void destroy(EulerTourTree.Splay s) {
        //buffer.release(s);
    }

    public int rootOf(int i) {
        return rootOf(nodes[i]).id;
    }

    public void setRoot(int i) {
        if (rootOf(i) == i) {
            return;
        }

        EulerTourTree.Splay.splay(nodes[i]);
        EulerTourTree.Splay l = EulerTourTree.Splay.splitLeft(nodes[i]);
        if (l == EulerTourTree.Splay.NIL) {
            return;
        }
        EulerTourTree.Splay a = EulerTourTree.Splay.selectMinAsRoot(l);
        EulerTourTree.Splay b = EulerTourTree.Splay.selectMaxAsRoot(nodes[i]);

        if (nodes[a.id] == a) {
            EulerTourTree.Splay.splitLeft(b);
            destroy(b);
        } else {
            l = EulerTourTree.Splay.splitRight(a);
            destroy(a);
        }

        EulerTourTree.Splay newNode = alloc(i);
        EulerTourTree.Splay.splay(nodes[i]);
        EulerTourTree.Splay.splay(l);
        EulerTourTree.Splay.merge(nodes[i], EulerTourTree.Splay.merge(l, newNode));
    }

    private long idOfEdge(int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        return (((long) i) << 32) | j;
    }

    public void link(int i, int j) {
        setRoot(i);
        setRoot(j);

        EulerTourTree.Edge e = new EulerTourTree.Edge();

        long id = idOfEdge(i, j);
        e.a = alloc(-1);
        e.b = alloc(-1);
        map.put(id, e);

        EulerTourTree.Splay.splay(nodes[i]);
        EulerTourTree.Splay.splay(nodes[j]);
        EulerTourTree.Splay.merge(nodes[i], e.a);
        EulerTourTree.Splay.merge(nodes[j], e.b);
        EulerTourTree.Splay.splay(nodes[i]);
        EulerTourTree.Splay.splay(nodes[j]);
        EulerTourTree.Splay.merge(nodes[i], nodes[j]);

        EulerTourTree.Splay newNode = alloc(i);
        EulerTourTree.Splay.splay(nodes[i]);
        EulerTourTree.Splay.merge(nodes[i], newNode);
    }

    private EulerTourTree.Splay rootOf(EulerTourTree.Splay x) {
        EulerTourTree.Splay.splay(x);
        return EulerTourTree.Splay.selectMinAsRoot(x);
    }

    public void cut(int i, int j) {
        long id = idOfEdge(i, j);
        EulerTourTree.Edge e = map.remove(id);

        EulerTourTree.Splay.splay(e.a);
        EulerTourTree.Splay al = EulerTourTree.Splay.splitLeft(e.a);
        EulerTourTree.Splay ar = EulerTourTree.Splay.splitRight(e.a);


        EulerTourTree.Splay l, r;
        if (rootOf(ar) == rootOf(e.b)) {
            EulerTourTree.Splay.splay(e.b);
            EulerTourTree.Splay bl = EulerTourTree.Splay.splitLeft(e.b);
            EulerTourTree.Splay br = EulerTourTree.Splay.splitRight(e.b);

            l = al;
            r = br;
        } else {
            EulerTourTree.Splay.splay(e.b);
            EulerTourTree.Splay bl = EulerTourTree.Splay.splitLeft(e.b);
            EulerTourTree.Splay br = EulerTourTree.Splay.splitRight(e.b);

            l = bl;
            r = ar;
        }

        EulerTourTree.Splay.splay(l);
        EulerTourTree.Splay.splay(r);
        l = EulerTourTree.Splay.selectMaxAsRoot(l);
        r = EulerTourTree.Splay.selectMinAsRoot(r);

        if (nodes[l.id] == l) {
            EulerTourTree.Splay rSnapshot = r;
            r = EulerTourTree.Splay.splitRight(r);
            destroy(rSnapshot);
        } else {
            EulerTourTree.Splay lSnapshot = l;
            l = EulerTourTree.Splay.splitLeft(l);
            destroy(lSnapshot);
        }

        EulerTourTree.Splay.merge(l, r);
        destroy(e.a);
        destroy(e.b);
    }

    private static class Edge {
        EulerTourTree.Splay a;
        EulerTourTree.Splay b;

    }

    public static class Splay implements Cloneable {
        public static final EulerTourTree.Splay NIL = new EulerTourTree.Splay();
        EulerTourTree.Splay left = NIL;
        EulerTourTree.Splay right = NIL;
        EulerTourTree.Splay father = NIL;
        int size;
        int id;
        long val;
        long sum;
        long dirty;
        int w;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.size = 0;
            NIL.id = -2;
        }

        public void modify(long x) {
            val += x;
            sum += x * size;
            dirty += x;
        }

        public static void splay(EulerTourTree.Splay x) {
            if (x == NIL) {
                return;
            }
            EulerTourTree.Splay y, z;
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

        public static void zig(EulerTourTree.Splay x) {
            EulerTourTree.Splay y = x.father;
            EulerTourTree.Splay z = y.father;
            EulerTourTree.Splay b = x.right;

            y.setLeft(b);
            x.setRight(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public static void zag(EulerTourTree.Splay x) {
            EulerTourTree.Splay y = x.father;
            EulerTourTree.Splay z = y.father;
            EulerTourTree.Splay b = x.left;

            y.setRight(b);
            x.setLeft(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public void setLeft(EulerTourTree.Splay x) {
            left = x;
            x.father = this;
        }

        public void setRight(EulerTourTree.Splay x) {
            right = x;
            x.father = this;
        }

        public void changeChild(EulerTourTree.Splay y, EulerTourTree.Splay x) {
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
            size = left.size + right.size + w;
            sum = left.sum + val * w + right.sum;
        }

        public void pushDown() {
            if (dirty != 0) {
                left.modify(dirty);
                right.modify(dirty);
                dirty = 0;
            }
        }

        public static void toString(EulerTourTree.Splay root, StringBuilder builder) {
            if (root == NIL) {
                return;
            }
            root.pushDown();
            toString(root.left, builder);
            builder.append(root.id).append(',');
            toString(root.right, builder);
        }

        public EulerTourTree.Splay clone() {
            try {
                return (EulerTourTree.Splay) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public static EulerTourTree.Splay cloneTree(EulerTourTree.Splay splay) {
            if (splay == NIL) {
                return NIL;
            }
            splay = splay.clone();
            splay.left = cloneTree(splay.left);
            splay.right = cloneTree(splay.right);
            return splay;
        }

        public static EulerTourTree.Splay selectMinAsRoot(EulerTourTree.Splay root) {
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

        public static EulerTourTree.Splay selectMaxAsRoot(EulerTourTree.Splay root) {
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

        public static EulerTourTree.Splay splitLeft(EulerTourTree.Splay root) {
            root.pushDown();
            EulerTourTree.Splay left = root.left;
            left.father = NIL;
            root.setLeft(NIL);
            root.pushUp();
            return left;
        }

        public static EulerTourTree.Splay splitRight(EulerTourTree.Splay root) {
            root.pushDown();
            EulerTourTree.Splay right = root.right;
            right.father = NIL;
            root.setRight(NIL);
            root.pushUp();
            return right;
        }

        public static EulerTourTree.Splay merge(EulerTourTree.Splay a, EulerTourTree.Splay b) {
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

        public String toString() {
            StringBuilder builder = new StringBuilder().append(id).append(":");
            toString(cloneTree(this), builder);
            return builder.toString();
        }

    }
}