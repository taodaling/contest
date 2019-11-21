package template.datastructure;

public class Tree01 {
    private Tree01[] children = new Tree01[]{NIL, NIL};
    int size;
    private static Tree01 NIL = new Tree01();

    static {
        NIL.children[0] = NIL.children[1] = NIL;
    }

    public void pushDown() {
    }

    public void pushUp() {
        size = children[0].size + children[1].size;
    }

    public static Tree01 insert(Tree01 root, int v, int offset) {
        if (offset < 0) {
            root.size++;
            return root;
        }
        if (root == NIL) {
            root = new Tree01();
        }
        int bit = (v >> offset) & 1;
        root.pushDown();
        root.children[bit] = insert(root.children[bit], v, offset - 1);
        root.pushUp();
        return root;
    }

    public static String toString(Tree01 root, StringBuilder builder) {
        if (root == NIL) {
            return "";
        }
        if (root.size == 1 && root.children[0] == NIL && root.children[1] == NIL) {
            return builder.toString() + "\n";
        }
        builder.append('0');
        String result = toString(root.children[0], builder);
        builder.setCharAt(builder.length() - 1, '1');
        result += toString(root.children[1], builder);
        builder.setLength(builder.length() - 1);
        return result;
    }

    @Override
    public String toString() {
        return toString(this, new StringBuilder());
    }
}
