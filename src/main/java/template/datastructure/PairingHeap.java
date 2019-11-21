package template.datastructure;

import java.util.Comparator;
import java.util.Objects;

public class PairingHeap<T> implements Cloneable {
    public static final PairingHeap NIL = new PairingHeap(null);

    static {
        NIL.children = NIL.prev = NIL.next = NIL.children;
    }

    PairingHeap<T> father = NIL;
    PairingHeap<T> prev = NIL;
    PairingHeap<T> next = NIL;
    PairingHeap<T> children = NIL;
    private T val;

    public PairingHeap(T val) {
        this.val = val;
    }

    private static <T> void detach(PairingHeap<T> father, PairingHeap<T> child) {
        if (father == NIL || child == NIL) {
            return;
        }

        child.prev.next = child.next;
        child.next.prev = child.prev;

        if (child.next == child) {
            father.children = NIL;
        } else if (father.children == child) {
            father.children = child.next;
        }
        child.next = child.prev = child.father = NIL;
    }

    private static <T> void attach(PairingHeap<T> father, PairingHeap<T> child) {
        if (father == NIL || child == NIL) {
            return;
        }

        if (father.children == NIL) {
            child.prev = child.next = child;
            child.father = father;
            father.children = child;
            return;
        }
        child.prev = father.children;
        child.next = father.children.next;
        child.prev.next = child;
        child.next.prev = child;
        child.father = father;
    }

    public static <T> PairingHeap<T> merge(PairingHeap<T> a, PairingHeap<T> b, Comparator<T> comp) {
        if (a == NIL) {
            return b;
        }
        if (b == NIL) {
            return a;
        }
        if (comp.compare(a.val, b.val) > 0) {
            PairingHeap<T> tmp = a;
            a = b;
            b = tmp;
        }
        attach(a, b);
        return a;
    }

    public static <T> T peek(PairingHeap<T> root) {
        return root.val;
    }

    public static <T> PairingHeap<T> decrease(PairingHeap<T> root, PairingHeap<T> node,
                                              T val, Comparator<T> comp) {
        if (root == node) {
            root.val = val;
            return root;
        }
        detach(node.father, node);
        node.val = val;
        return merge(root, node, comp);
    }

    private static <T> PairingHeap<T> popByLoop(PairingHeap<T> head, Comparator<T> comp) {
        PairingHeap<T> loop = head;
        loop.father = NIL;
        loop.father.children = NIL;
        for (PairingHeap<T> i = loop.next; i != loop; i = i.next) {
            loop.father = NIL;
        }

        PairingHeap<T> begin = loop;
        PairingHeap<T> end = begin.prev;
        end.next = NIL;

        while (begin.next != NIL) {
            PairingHeap<T> next = begin.next.next;
            PairingHeap<T> m = merge(begin, begin.next, comp);
            m.next = NIL;
            begin = next;
            if (begin == NIL) {
                begin = end = m;
            } else {
                end.next = m;
                end = m;
            }
        }
        begin.prev = begin.next = NIL;
        return begin;
    }

    private static <T> PairingHeap<T> pop0(PairingHeap<T> head, Comparator<T> comp) {
        if (head.next == head) {
            detach(head.father, head);
            return head;
        }
        PairingHeap<T> prev = head.prev;
        PairingHeap<T> next = head.next;
        detach(head.father, head);
        detach(next.father, next);
        PairingHeap<T> m = merge(head, next, comp);
        if (prev != next) {
            m = merge(m, pop0(prev, comp), comp);
        }
        return m;
    }

    public static <T> PairingHeap<T> pop(PairingHeap<T> root, Comparator<T> comp) {
        if (root.children == NIL) {
            return NIL;
        }
        return popByLoop(root.children, comp);
    }

    public static <T> boolean isEmpty(PairingHeap<T> root) {
        return root == NIL;
    }

    public PairingHeap<T> clone() {
        PairingHeap<T> ans = new PairingHeap<>(this.val);
        if (children == NIL) {
            return ans;
        }
        attach(ans, children.clone());
        for (PairingHeap<T> i = children.next; i != children; i = i.next) {
            attach(ans, i.clone());
        }
        return ans;
    }

    public String toString(Comparator<T> comp) {
        StringBuilder builder = new StringBuilder();
        PairingHeap<T> heap = clone();
        while (!isEmpty(heap)) {
            builder.append(heap.val).append(',');
            heap = pop(heap, comp);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return Objects.toString(val);
    }
}
