package template.datastructure;

import java.util.Comparator;

public class MergeAbleHeap<V> {
    private LeftistTree<V> tree = LeftistTree.NIL;
    private int size;

    public void add(V element, Comparator<V> comparator) {
        tree = LeftistTree.merge(tree, new LeftistTree<>(element), comparator);
        size++;
    }

    public V peek() {
        return tree.peek();
    }

    public V pop(Comparator<V> comparator) {
        V ans = tree.peek();
        tree = LeftistTree.pop(tree, comparator);
        size--;
        return ans;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void merge(MergeAbleHeap<V> heap, Comparator<V> comparator) {
        tree = LeftistTree.merge(tree, heap.tree, comparator);
        size += heap.size;
        heap.tree = LeftistTree.NIL;
        heap.size = 0;
    }
}