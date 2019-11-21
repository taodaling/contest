package template.datastructure;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;

public class LeftistTree<K> {
    public static final LeftistTree NIL = new LeftistTree<>(null);

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.dist = -1;
    }

    LeftistTree<K> left = NIL;
    LeftistTree<K> right = NIL;
    int dist;
    K key;

    public LeftistTree(K key) {
        this.key = key;
    }

    public static <K> LeftistTree<K> createFromCollection(Collection<LeftistTree<K>> trees, Comparator<K> cmp) {
        return createFromDeque(new ArrayDeque<>(trees), cmp);
    }

    public static <K> LeftistTree<K> createFromDeque(Deque<LeftistTree<K>> deque, Comparator<K> cmp) {
        while (deque.size() > 1) {
            deque.addLast(merge(deque.removeFirst(), deque.removeFirst(), cmp));
        }
        return deque.removeLast();
    }

    public static <K> LeftistTree<K> merge(LeftistTree<K> a, LeftistTree<K> b, Comparator<K> cmp) {
        if (a == NIL) {
            return b;
        } else if (b == NIL) {
            return a;
        }
        if (cmp.compare(a.key, b.key) > 0) {
            LeftistTree<K> tmp = a;
            a = b;
            b = tmp;
        }
        a.right = merge(a.right, b, cmp);
        if (a.left.dist < a.right.dist) {
            LeftistTree<K> tmp = a.left;
            a.left = a.right;
            a.right = tmp;
        }
        a.dist = a.right.dist + 1;
        return a;
    }

    public boolean isEmpty() {
        return this == NIL;
    }

    public K peek() {
        return key;
    }

    public static <K> LeftistTree<K> pop(LeftistTree<K> root, Comparator<K> cmp) {
        return merge(root.left, root.right, cmp);
    }

    private void toStringDfs(StringBuilder builder) {
        if (this == NIL) {
            return;
        }
        builder.append(key).append(' ');
        left.toStringDfs(builder);
        right.toStringDfs(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toStringDfs(builder);
        return builder.toString();
    }
}