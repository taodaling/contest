package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;

public class EPlanOfLectures {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        List<Node> seq = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int pre = in.ri() - 1;
            if (pre == -1) {
                continue;
            }
            nodes[i].pre.add(nodes[pre]);
        }
        for (int i = 0; i < k; i++) {
            Node x = nodes[in.ri() - 1];
            Node y = nodes[in.ri() - 1];
            y.pre.add(x);
            concat(x, y);
        }
        if (!valid) {
            out.println(0);
            return;
        }
        for (Node node : nodes) {
            if (node.next != null) {
                continue;
            }
            Set<Node> all = new HashSet<>();
            Node cur = node;
            while (true) {
                all.addAll(cur.pre);
                all.remove(cur);
                cur.pre.clear();
                if (cur.prev != null) {
                    cur.pre.add(cur.prev);
                    cur = cur.prev;
                } else {
                    cur.pre = new ArrayList<>(all);
                    break;
                }
            }
        }
        Deque<Node> dq = new ArrayDeque<>(n);
        for (Node node : nodes) {
            node.deg = node.pre.size();
            if (node.deg == 0) {
                dq.add(node);
            }
            for (Node x : node.pre) {
                x.rely.add(node);
            }
        }
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();

            debug.debug("head", head);
            seq.add(head);
            for (Node node : head.rely) {
                node.deg--;
                if (node.deg == 0) {
                    if (node == head.next) {
                        dq.addFirst(node);
                    } else {
                        dq.addLast(node);
                    }
                }
            }
        }
        if (seq.size() < n) {
            out.println(0);
            return;
        }
        for (Node node : seq) {
            out.append(node.id + 1).append(' ');
        }
    }

    boolean valid = true;

    public void concat(Node a, Node b) {
        if (a.next != null || b.prev != null) {
            valid = false;
        }
        a.next = b;
        b.prev = a;
    }
}

class Node {
    int id;
    List<Node> pre = new ArrayList<>();
    List<Node> rely = new ArrayList<>();
    Node prev;
    Node next;
    int deg;

    @Override
    public String toString() {
        return "" + id;
    }
}
