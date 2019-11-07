package contest;


import java.util.HashSet;
import java.util.Set;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.set.add(b);
            b.set.add(a);
        }

        LinkedList wait = new LinkedList();
        LinkedList pend = new LinkedList();
        for (int i = 1; i <= n; i++) {
            wait.addLast(nodes[i]);
        }
        pend.addLast(wait.remove(wait.head));

        int fee = 0;
        while (wait.head != null) {
            if (pend.head == null) {
                fee++;
                pend.addLast(wait.remove(wait.head));
                continue;
            }
            Node head = pend.remove(pend.head);
            for (Node node = wait.head, next; node != null; node = next) {
                next = node.next;
                if (head.set.contains(node)) {
                    continue;
                }
                pend.addLast(wait.remove(node));
            }
        }

        out.println(fee);
    }

}


class Node {
    Set<Node> set = new HashSet<>();
    int id;
    Node next;
    Node prev;
}


class LinkedList {
    Node head = null;
    Node tail = null;
    int size;

    void addLast(Node node) {
        size++;
        if (tail == null) {
            head = tail = node;
            return;
        }
        tail.next = node;
        node.prev = tail;
        tail = node;
    }

    Node remove(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        if (node == head) {
            head = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        }
        size--;
        node.next = node.prev = null;
        return node;
    }
}
