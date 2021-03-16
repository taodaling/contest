package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;

import java.util.ArrayList;
import java.util.List;

public class CSubstitutesInNumber {
    int mod = (int) 1e9 + 7;

    Node nil = new Node();

    {
        nil.sum = 0;
        nil.p10 = 1;
    }

    private Node build(char[] s, int l, int r) {
        Node head = null;
        Node tail = null;
        for (int i = l; i <= r; i++) {
            Node node = new Node();
            node.val = s[i] - '0';
            if (head == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }
        if (tail == null) {
            return nil;
        }
        tail.next = nil;
        return head;
    }

    void register(Node head) {
        for (Node node = head; node != nil; node = node.next) {
            register[node.val].add(node);
        }
    }

    List<Node>[] register = Graph.createGraph(10);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.rs(s);
        Node head = build(s, 0, n - 1);
        register(head);
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            n = in.rs(s);
            int v = s[0] - '0';
            Node rep = build(s, 3, n - 1);
            for (Node node : register[v]) {
                node.cast = rep;
            }
            register[v].clear();
            register(rep);
        }

        long ans = sum(head);
        out.println(ans);
    }

    long p10(Node node) {
        if (node.p10 == -1) {
            node.p10 = p10(node.next);
            if (node.cast != null) {
                node.p10 = node.p10 * p10(node.cast);
            } else {
                node.p10 = node.p10 * 10;
            }
            node.p10 %= mod;
        }
        return node.p10;
    }

    long sum(Node node) {
        if (node.sum == -1) {
            node.sum = sum(node.next);
            if (node.cast != null) {
                node.sum += sum(node.cast) * p10(node.next);
            } else {
                node.sum += node.val * p10(node.next);
            }
            node.sum %= mod;
        }
        return node.sum;
    }
}

class Node {
    int val;
    long p10 = -1;
    long sum = -1;
    Node next;
    Node cast;

    @Override
    public String toString() {
        if (next == null) {
            return "";
        }
        if (cast == null) {
            return "" + val + next.toString();
        } else {
            return "" + cast + next.toString();
        }
    }
}
