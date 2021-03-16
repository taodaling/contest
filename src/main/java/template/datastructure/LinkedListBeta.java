package template.datastructure;

import template.utils.RevokeIterator;

import java.util.Objects;

public class LinkedListBeta<E> implements Iterable<E> {
    public static class Node<E> extends CircularLinkedNode<Node<E>> {
        public E val;

        public Node(E val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return Objects.toString(val);
        }
    }

    public Node<E> dummy = new Node<>(null);
    protected int size;

    public Node<E> addFirst(E e) {
        Node node = new Node(e);
        return addFirst(node);
    }

    public Node<E> begin() {
        return dummy.next;
    }

    public Node<E> rbegin() {
        return dummy.prev;
    }

    public Node<E> end() {
        return dummy;
    }

    public Node<E> addFirst(Node<E> node) {
        node.attach(dummy);
        size++;
        return node;
    }

    public Node<E> addLast(E e) {
        Node<E> node = new Node<>(e);
        return addLast(node);
    }

    public Node<E> addAfter(Node<E> node, E e) {
        return addAfter(node, new Node<>(e));
    }

    public Node<E> addAfter(Node<E> node, Node<E> follow) {
        follow.attach(node);
        size++;
        return follow;
    }

    public Node<E> addLast(Node<E> node) {
        return addAfter(dummy.prev, node);
    }

    public void remove(Node<E> node) {
        assert contain(node);
        node.detach();
        size--;
        assert size >= 0;
    }

    public boolean contain(Node<E> node) {
        for (Node<E> head = begin(); head != end(); head = head.next) {
            if (head == node) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public RevokeIterator<E> iterator() {
        return new RevokeIterator<E>() {
            Node<E> trace = dummy;

            @Override
            public void revoke() {
                trace = trace.prev;
            }

            @Override
            public boolean hasNext() {
                return trace.next != dummy;
            }

            @Override
            public E next() {
                return (trace = trace.next).val;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (E e : this) {
            ans.append(e).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append(']');
        return ans.toString();
    }
}
