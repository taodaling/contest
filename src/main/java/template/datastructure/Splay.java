package template.datastructure;

/**
 * Created by dalt on 2018/5/20.
 */
public class Splay implements Cloneable {
    public static final Splay NIL = new Splay();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.size = 0;
    }

    Splay left = NIL;
    Splay right = NIL;
    Splay father = NIL;
    int size = 1;
    int key;

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
        if(this == NIL){
            return;
        }
        size = left.size + right.size + 1;
    }

    public void pushDown() {
        if(this == NIL){
            return;
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
