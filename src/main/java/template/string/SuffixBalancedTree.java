package template.string;

import java.util.ArrayDeque;
import java.util.Deque;

public class SuffixBalancedTree {

    private static final double FACTOR = 0.75;
    private static Node[] stk = new Node[0];
    private static int tail;
    private Node root;
    private IntHolder intHolder = new IntHolder();
    private ObjectHolder<Node> objectHolder = new ObjectHolder<>();
    private Deque<Node> dq = new ArrayDeque<>();

    private static class IntHolder {
        public int size;

        public void clear() {
            size = 0;
        }
    }

    private static class ObjectHolder<V> {
        V data;

        public void clear() {
            data = null;
        }
    }

    public SuffixBalancedTree() {
        root = Node.NIL;

        Node dummy = new Node(Integer.MIN_VALUE);
        dummy.next = dummy;
        dummy.occur = 0;
        dummy.offsetToTail = -1;
        dummy.weight = 0;
        dq.addFirst(dummy);
    }

    private boolean check() {
        collect(root);
        for (int i = 1; i < tail; i++) {
            if (stk[i - 1].weight >= stk[i].weight) {
                return false;
            }
            if (compare(stk[i - 1], stk[i]) >= 0) {
                return false;
            }
        }
        for (int i = 0; i < tail; i++) {
            if (stk[i].occur < 0) {
                return false;
            }
        }

        if (root.liveSize + 1 != dq.size()) {
            return false;
        }

        return true;
    }

    public Node addPrefix(int x) {
        objectHolder.clear();
        root = insert(root, x, dq.peekFirst(), objectHolder, 0, 1);
        Node node = objectHolder.data;
        dq.addFirst(node);
        assert check();
        return node;
    }

    public void removePrefix() {
        assert dq.size() > 1;
        root = delete(root, dq.removeFirst(), 0, 1);
        assert check();

        //clean or not
        if (root.liveSize * 2 < root.size) {
            collect(root);
            int wpos = 0;
            for (int i = 0; i < tail; i++) {
                if (stk[i].occur == 0) {
                    continue;
                }
                stk[wpos++] = stk[i];
            }
            root = refactor(0, wpos - 1, 0, 1);
        }
    }

    public int rank(Node node) {
        intHolder.clear();
        root = rank(root, node, intHolder, 0, 1);
        return intHolder.size;
    }

    public int leq(IntSequence seq) {
        intHolder.clear();
        root = rank(root, seq, intHolder, 0, 1);
        return intHolder.size;
    }

    public Node sa(int k) {
        k++;
        objectHolder.clear();
        root = sa(root, k, objectHolder, 0, 1);
        return objectHolder.data;
    }

    public int[] sa() {
        collect(root);
        int[] sa = new int[size()];
        int wpos = 0;
        for (int i = 0; i < tail; i++) {
            if (stk[i].occur == 0) {
                continue;
            }
            sa[wpos++] = stk[i].offsetToTail;
        }
        return sa;
    }

    public int size() {
        return root.liveSize;
    }

    private static void ensureSpace(int n) {
        if (stk.length >= n) {
            return;
        }
        int nextSize = Math.max(1 << 16, stk.length);
        while (nextSize < n) {
            nextSize += nextSize;
        }
        stk = new Node[nextSize];
    }

    private static int insertCompare(Node a, int key, Node next) {
        if (a.key != key) {
            return Integer.compare(a.key, key);
        }
        return Double.compare(a.next.weight, next.weight);
    }

    private static int compare(Node root, IntSequence seq) {
        int len = seq.length();
        for (int i = 0; i < len; i++, root = root.next) {
            if (seq.get(i) != root.key) {
                return Integer.compare(root.key, seq.get(i));
            }
        }
        return 0;
    }

    private static int compare(Node a, Node b) {
        for (int i = 0; a != b; i++, a = a.next, b = b.next) {
            if (a.key != b.key) {
                return Integer.compare(a.key, b.key);
            }
        }
        return 0;
    }

    private static Node rank(Node root, IntSequence seq, IntHolder count, double L, double R) {
        if (root == Node.NIL) {
            return root;
        }

        root = refactor(root, L, R);
        root.pushDown();
        int compRes = compare(root, seq);
        if (compRes > 0) {
            root.left = rank(root.left, seq, count, L, root.weight);
        } else {
            count.size += root.liveSize - root.right.liveSize;
            root.right = rank(root.right, seq, count, root.weight, R);
        }
        root.pushUp();
        return root;
    }

    private static Node rank(Node root, Node node, IntHolder count, double L, double R) {
        if (root == Node.NIL) {
            return root;
        }
        root = refactor(root, L, R);
        root.pushDown();
        if (root == node) {
            count.size += root.liveSize - root.right.liveSize;
        } else {
            int compRes = root.compareTo(node);
            if (compRes > 0) {
                root.left = rank(root.left, node, count, L, root.weight);
            } else {
                count.size += root.liveSize - root.right.liveSize;
                root.right = rank(root.right, node, count, root.weight, R);
            }
        }
        root.pushUp();
        return root;
    }

    private static Node sa(Node root, int k, ObjectHolder<Node> count, double L, double R) {
        if (root == Node.NIL) {
            return root;
        }
        root = refactor(root, L, R);
        root.pushDown();

        if (root.left.liveSize >= k) {
            root.left = sa(root.left, k, count, L, root.weight);
        } else if (root.liveSize - root.right.liveSize == k) {
            count.data = root;
        } else {
            k -= root.liveSize - root.right.liveSize;
            root.right = sa(root.right, k, count, root.weight, R);
        }

        root.pushUp();
        return root;
    }

    private static Node newNode(int key, Node next, double weight) {
        Node root = new Node();
        root.key = key;
        root.weight = weight;
        root.next = next;
        root.occur++;
        root.offsetToTail = next.offsetToTail + 1;
        root.pushUp();
        return root;
    }

    private static Node insert(Node root, int key, Node next, ObjectHolder<Node> insertNode, double L, double R) {
        if (root == Node.NIL) {
            root = newNode(key, next, (L + R) / 2);
            insertNode.data = root;
            return root;
        }
        root = refactor(root, L, R);
        root.pushDown();
        int cmpRes = insertCompare(root, key, next);
        if (cmpRes == 0) {
            insertNode.data = root;
            root.occur++;
        } else if (cmpRes > 0) {
            root.left = insert(root.left, key, next, insertNode, L, root.weight);
        } else {
            root.right = insert(root.right, key, next, insertNode, root.weight, R);
        }
        root.pushUp();
        return root;
    }

    private static Node delete(Node root, Node node, double L, double R) {
        assert root != Node.NIL;
        root = refactor(root, L, R);
        root.pushDown();
        if (root == node) {
            root.occur--;
        } else {
            int compRes = root.compareTo(node);
            if (compRes > 0) {
                root.left = delete(root.left, node, L, root.weight);
            } else {
                root.right = delete(root.right, node, root.weight, R);
            }
        }
        root.pushUp();
        return root;
    }

    private static void collect(Node root) {
        ensureSpace(root.size);
        tail = 0;
        collect0(root);
        assert tail == root.size;
    }


    private static Node refactor(Node root, double L, double R) {
        double threshold = root.size * FACTOR;
        if (root.left.size > threshold || root.right.size > threshold) {
            collect(root);
            root = refactor(0, tail - 1, L, R);
        }
        return root;
    }

    private static void collect0(Node root) {
        if (root == Node.NIL) {
            return;
        }
        root.pushDown();
        collect0(root.left);
        stk[tail++] = root;
        collect0(root.right);
    }

    private static Node refactor(int l, int r, double L, double R) {
        if (l > r) {
            return Node.NIL;
        }
        int m = (l + r) / 2;
        Node root = stk[m];
        root.weight = (L + R) / 2;
        root.left = refactor(l, m - 1, L, root.weight);
        root.right = refactor(m + 1, r, root.weight, R);
        root.pushUp();
        return root;
    }


    @Override
    public String toString() {
        collect(root);
        StringBuilder ans = new StringBuilder("{");
        for (int i = 0; i < tail; i++) {
            ans.append(stk[i]).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("}");
        return ans.toString();
    }

    public static class Node implements Cloneable, Comparable<Node> {
        public static final Node NIL = new Node();

        Node left = NIL;
        Node right = NIL;
        int size;
        int liveSize;
        int key;
        int occur;
        public double weight;
        Node next;
        public int offsetToTail;

        static {
            NIL.left = NIL.right = NIL;
            NIL.size = NIL.liveSize = 0;
            NIL.key = -1;
            NIL.offsetToTail = -1;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(weight, o.weight);
        }

        public void pushUp() {
            if (this == NIL) {
                return;
            }
            size = left.size + right.size + 1;
            liveSize = left.liveSize + right.liveSize + occur;
        }

        public void pushDown() {

        }

        private Node() {
        }

        private Node(int key) {
            this.key = key;
            pushUp();
        }


        @Override
        public String toString() {
            StringBuilder ans = new StringBuilder("[");
            int remain = 10;
            Node node = this;
            for (; node != null && remain > 0; node = node.next == node ? null : node.next, remain--) {
                ans.append(node.key).append(',');
            }
            if (node != null) {
                ans.append(",...,");
            }
            if (ans.length() > 1) {
                ans.setLength(ans.length() - 1);
            }
            ans.append("]");
            return ans.toString();
        }
    }
}
