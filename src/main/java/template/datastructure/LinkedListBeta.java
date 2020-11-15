package template.datastructure;

import template.utils.RevokeIterator;

public class LinkedListBeta<E> implements Iterable<E> {
    public static class Node<E> extends CircularLinkedNode<Node<E>> {
        public E val;

        public Node(E val) {
            this.val = val;
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
        Node node = new Node(e);
        return addLast(node);
    }

    public Node<E> addLast(Node node) {
        node.attach(dummy.prev);
        size++;
        return node;
    }

    public void remove(Node<E> node) {
        node.detach();
        size--;
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
}
