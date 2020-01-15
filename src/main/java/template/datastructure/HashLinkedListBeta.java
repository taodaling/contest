package template.datastructure;

import java.util.HashMap;
import java.util.Map;

public class HashLinkedListBeta<E> extends LinkedListBeta<E> {
    private Map<E, Node<E>> map;

    public HashLinkedListBeta(int cap) {
        map = new HashMap<>(cap);
    }

    public HashLinkedListBeta() {
        this(0);
    }

    private void assertNull(Object o) {
        if (o != null) {
            throw new IllegalStateException();
        }
    }

    private void assertNotNull(Object o) {
        if (o == null) {
            throw new IllegalStateException();
        }
    }

    @Override
    public Node<E> addFirst(E e) {
        Node<E> ans = super.addFirst(e);
        assertNull(map.put(e, ans));
        return ans;
    }

    @Override
    public Node<E> addLast(E e) {
        Node<E> ans = super.addLast(e);
        assertNull(map.put(e, ans));
        return ans;
    }

    @Override
    public void remove(Node<E> node) {
        super.remove(node);
        assertNotNull(map.remove(node.val));
    }

    public Node<E> getNodeByVal(E val) {
        return map.get(val);
    }
}
