package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.primitve.generated.LongObjectHashMap;
import template.utils.Buffer;

public class LUOGU4219 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        EulerTourTree ett = new EulerTourTree(n);
        for (int i = 0; i < q; i++) {
            char c = in.readChar();
            int x = in.readInt() - 1;
            int y = in.readInt() - 1;
            if (c == 'A') {
                ett.link(x, y);
            }else{
                ett.cut(x, y);
                ett.setRoot(x);
                ett.setRoot(y);
                EulerTourTree.Splay a = ett.nodes[x];
                EulerTourTree.Splay b = ett.nodes[y];
                EulerTourTree.Splay.splay(a);
                EulerTourTree.Splay.splay(b);
                long ans = (long)a.size * b.size;
                out.println(ans);
                ett.link(x, y);
            }
        }
    }
}

class EulerTourTree {
    Splay[] nodes;
    LongObjectHashMap<Edge> map;
    Buffer<Splay> buffer = new Buffer<>(Splay::new);

    public EulerTourTree(int n) {
        map = new LongObjectHashMap<>(n, true);
        nodes = new Splay[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = alloc(i);
            nodes[i].weight = 1;
            nodes[i].pushUp();
        }
    }

    private Splay alloc(int id) {
        Splay splay = new Splay();//buffer.alloc();
        splay.id = id;
        return splay;
    }

    private void destroy(Splay s) {
        //buffer.release(s);
    }

    public int rootOf(int i) {
        return rootOf(nodes[i]).id;
    }

    public void setRoot(int i) {
        if (rootOf(i) == i) {
            return;
        }

        Splay.splay(nodes[i]);
        Splay l = Splay.splitLeft(nodes[i]);
        if (l == Splay.NIL) {
            return;
        }
        Splay a = Splay.selectMinAsRoot(l);
        Splay b = Splay.selectMaxAsRoot(nodes[i]);

        if (nodes[a.id] == a) {
            Splay.splitLeft(b);
            destroy(b);
        } else {
            l = Splay.splitRight(a);
            destroy(a);
        }

        Splay newNode = alloc(i);
        Splay.splay(nodes[i]);
        Splay.splay(l);
        Splay.merge(nodes[i], Splay.merge(l, newNode));
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

        Edge e = new Edge();

        long id = idOfEdge(i, j);
        e.a = alloc(-1);
        e.b = alloc(-1);
        map.put(id, e);

        Splay.splay(nodes[i]);
        Splay.splay(nodes[j]);
        Splay.merge(nodes[i], e.a);
        Splay.merge(nodes[j], e.b);
        Splay.splay(nodes[i]);
        Splay.splay(nodes[j]);
        Splay.merge(nodes[i], nodes[j]);

        Splay newNode = alloc(i);
        Splay.splay(nodes[i]);
        Splay.merge(nodes[i], newNode);
    }

    private Splay rootOf(Splay x) {
        Splay.splay(x);
        return Splay.selectMinAsRoot(x);
    }

    public void cut(int i, int j) {
        long id = idOfEdge(i, j);
        Edge e = map.remove(id);

        Splay.splay(e.a);
        Splay al = Splay.splitLeft(e.a);
        Splay ar = Splay.splitRight(e.a);


        Splay l, r;
        if (rootOf(ar) == rootOf(e.b)) {
            Splay.splay(e.b);
            Splay bl = Splay.splitLeft(e.b);
            Splay br = Splay.splitRight(e.b);

            l = al;
            r = br;
        } else {
            Splay.splay(e.b);
            Splay bl = Splay.splitLeft(e.b);
            Splay br = Splay.splitRight(e.b);

            l = bl;
            r = ar;
        }

        Splay.splay(l);
        Splay.splay(r);
        l = Splay.selectMaxAsRoot(l);
        r = Splay.selectMinAsRoot(r);

        if (nodes[l.id] == l) {
            Splay rSnapshot = r;
            r = Splay.splitRight(r);
            destroy(rSnapshot);
        } else {
            Splay lSnapshot = l;
            l = Splay.splitLeft(l);
            destroy(lSnapshot);
        }

        Splay.merge(l, r);
        destroy(e.a);
        destroy(e.b);
    }

    private static class Edge {
        Splay a;
        Splay b;
    }

    /**
     * Created by dalt on 2018/5/20.
     */
    public static class Splay implements Cloneable {
        public static final Splay NIL = new Splay();

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.size = 0;
            NIL.id = -2;
        }

        Splay left = NIL;
        Splay right = NIL;
        Splay father = NIL;
        int size = 0;
        int weight = 0;
        int id;

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
            size = left.size + right.size + weight;
        }

        public void pushDown() {
        }

        public static void toString(Splay root, StringBuilder builder) {
            if (root == NIL) {
                return;
            }
            root.pushDown();
            toString(root.left, builder);
            builder.append(root.id).append(',');
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

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder().append(id).append(":");
            toString(cloneTree(this), builder);
            return builder.toString();
        }
    }
}
