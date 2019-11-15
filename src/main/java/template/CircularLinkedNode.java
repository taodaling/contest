package template;

public class CircularLinkedNode<T extends CircularLinkedNode<T>> {
    public T prev = (T) this;
    public T next = (T) this;

    /**
     * Attach this after node
     */
    public void attach(T node) {
        this.next = node.next;
        this.prev = node;
        this.next.prev = (T) this;
        this.prev.next = (T) this;
    }

    /**
     * detach from the circular linked list current in
     */
    public void detach() {
        this.prev.next = next;
        this.next.prev = prev;
        this.next = this.prev = (T) this;
    }
}
