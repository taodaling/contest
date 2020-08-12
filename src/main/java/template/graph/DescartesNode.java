package template.graph;

import template.primitve.generated.datastructure.IntegerComparator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

/**
 * 笛卡尔树，中序遍历为下标递增，根元素为最小元素（如果有多个，下标较小的作为根）
 */
public class DescartesNode {

    public int index;
    public DescartesNode left;
    public DescartesNode right;

    @Override
    public String toString() {
        return "" + index;
    }



    public static<T extends DescartesNode> T build(int l, int r, IntegerComparator comp, Supplier<T> supplier) {
        int len = r - l + 1;
        DescartesNode[] nodes = new DescartesNode[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = supplier.get();
            nodes[i].index = i + l;
        }
        Deque<DescartesNode> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comp.compare(deque.peekLast().index, nodes[i].index) > 0) {
                DescartesNode tail = deque.removeLast();
                tail.right = nodes[i].left;
                nodes[i].left = tail;
            }
            deque.addLast(nodes[i]);
        }
        while (deque.size() > 1) {
            DescartesNode tail = deque.removeLast();
            deque.peekLast().right = tail;
        }
        return (T)deque.removeLast();
    }
}
