package on2021_03.on2021_03_30_CS_Academy___Virtual_FII_Code_2020_Round__1__rated_for_all_.Driveaway;



import template.io.FastInput;
import template.io.FastOutput;

import javax.naming.NoInitialContextException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Driveaway {
    void concat(Node a, Node b) {
        a.next = b;
        b.prev = a;
    }

    void cut(Node x) {
        if (x.prev != null) {
            x.prev.next = x.next;
        }
        if (x.next != null) {
            x.next.prev = x.prev;
        }
        x.prev = x.next = null;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int[] a = in.ri(n);
        long sum = 0;
        for (int t : a) {
            sum += t;
        }
        Node[] nodes = new Node[n - 1];
        for (int i = 0; i < n - 1; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].weight = Math.min(x, a[i] + a[i + 1]);
            if (i > 0) {
                concat(nodes[i - 1], nodes[i]);
            }
        }
        Node first = new Node();
        first.weight = (long) -1e18;
        Node last = new Node();
        last.weight = (long) -1e18;
        concat(first, nodes[0]);
        concat(nodes[n - 2], last);

        long reduce = 0;
        TreeSet<Node> set = new TreeSet<>(Comparator.<Node>comparingLong(t -> t.weight).thenComparingInt(t -> t.id));
        set.addAll(Arrays.asList(nodes));
        for (int i = 0; i < n / 2; i++) {
            Node head = set.pollLast();
            reduce += head.weight;
            Node prev = head.prev;
            Node next = head.next;
            cut(head);
            set.remove(prev);
            set.remove(next);
            prev.weight = prev.weight + next.weight - head.weight;
            cut(next);
            set.add(prev);
            out.println(sum - reduce);
        }
    }
}

class Node {
    int id;
    Node prev;
    Node next;
    long weight;
}