package template.datastructure;

import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class Scapegoat implements Cloneable {
    public static final Scapegoat NIL = new Scapegoat();
    public static final double FACTOR = 0.75;
    public static final List<Scapegoat> RECORDER = new ArrayList();

    Scapegoat left = NIL;
    Scapegoat right = NIL;
    int size;
    int key;
    boolean trashed;

    static {
        NIL.left = NIL.right = NIL;
        NIL.size = NIL.key = 0;
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
        left = check(left);
        right = check(right);
    }

    private Scapegoat() {
    }

    private Scapegoat(int key) {
        this.key = key;
        pushUp();
    }

    public Scapegoat insert(Scapegoat node) {
        if (this == NIL) {
            return node;
        }
        pushDown();
        if (key >= node.key) {
            left = left.insert(node);
        } else {
            right = right.insert(node);
        }
        pushUp();
        return this;
    }

    public Scapegoat delete(Scapegoat node) {
        if (this == NIL) {
            return NIL;
        }
        pushDown();
        if (key == node.key) {
            node.trashed = true;
        } else if (key >= node.key) {
            left = left.delete(node);
        } else {
            right = right.delete(node);
        }
        pushUp();
        return this;
    }

    @Override
    protected Scapegoat clone() {
        if (this == NIL) {
            return NIL;
        }
        try {
            Scapegoat scapegoat = (Scapegoat) super.clone();
            scapegoat.left = scapegoat.left.clone();
            scapegoat.right = scapegoat.right.clone();
            return scapegoat;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Scapegoat check(Scapegoat root) {
        double limit = root.size * FACTOR;
        if (root.left.size > limit || root.right.size > limit) {
            return refactor(root);
        }
        return root;
    }

    private static Scapegoat refactor(Scapegoat root) {
        RECORDER.clear();
        travel(root);
        return rebuild(0, RECORDER.size() - 1);
    }

    private void init() {
    }

    private static Scapegoat rebuild(int l, int r) {
        if (l > r) {
            return NIL;
        }
        int m = DigitUtils.floorAverage(l, r);
        Scapegoat root = RECORDER.get(m);
        root.init();
        root.left = rebuild(l, m - 1);
        root.right = rebuild(m + 1, r);
        root.pushUp();
        return root;
    }

    private static void travel(Scapegoat root) {
        if (root == NIL) {
            return;
        }
        travel(root.left);
        if (!root.trashed) {
            RECORDER.add(root);
        }
        travel(root.right);
    }

    public void toString(StringBuilder builder) {
        if (this == NIL) {
            return;
        }
        pushDown();
        left.toString(builder);
        if (!trashed) {
            builder.append(key).append(',');
        }
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        clone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}