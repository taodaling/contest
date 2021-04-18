package template.graph;

import template.primitve.generated.datastructure.IntegerComparator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

/**
 * 笛卡尔树，中序遍历为下标递增，根元素为最小元素（如果有多个，下标较小的作为根）
 */
public class DescartesNode<T extends DescartesNode<T>> {

    public int index;
    public T left;
    public T right;

    @Override
    public String toString() {
        return "" + index;
    }

    public static<T extends DescartesNode<T>> T build(int l, int r, IntegerComparator comp, Supplier<T> supplier) {
        int len = r - l + 1;
        DescartesNode<T>[] nodes = new DescartesNode[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = supplier.get();
            nodes[i].index = i + l;
        }
        Deque<T> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comp.compare(deque.peekLast().index, nodes[i].index) > 0) {
                T tail = deque.removeLast();
                tail.right = nodes[i].left;
                nodes[i].left = tail;
            }
            deque.addLast((T) nodes[i]);
        }
        while (deque.size() > 1) {
            T tail = deque.removeLast();
            deque.peekLast().right = tail;
        }
        return deque.removeLast();
    }
}
