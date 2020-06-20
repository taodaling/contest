package contest;

import sun.plugin2.gluegen.runtime.StructAccessor;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Babies {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] arrange = new int[n];
        Node[] boys = new Node[n];
        Node[] girls = new Node[n];
        for (int i = 0; i < n; i++) {
            boys[i] = new Node();
            boys[i].boy = true;
            girls[i] = new Node();
            girls[i].boy = false;
            boys[i].id = girls[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            int girlId = in.readInt() - 1;
            if (girlId < 0) {
                continue;
            }
            Node to = girls[girlId];

            if (boys[i].find() != to.find()) {
                boys[i].adj = to;
                to.incoming++;
                Node.merge(boys[i], to);
            }
        }

        for (int i = 0; i < n; i++) {
            int boyId = in.readInt() - 1;
            if (boyId < 0) {
                continue;
            }
            Node to = boys[boyId];

            if (girls[i].find() != to.find()) {
                to.incoming++;
                girls[i].adj = to;
                Node.merge(girls[i], to);
            }
        }

        trace = new ArrayDeque<>(2 * n);

        Stream.concat(Arrays.stream(boys), Arrays.stream(girls)).filter(x -> x.incoming == 0).forEach(node -> {
            trace.clear();
            dfs(node);
            while (trace.size() >= 2) {
                Node head = trace.removeFirst();
                Node match = trace.removeFirst();
                if (!head.boy) {
                    Node tmp = head;
                    head = match;
                    match = tmp;
                }
                arrange[head.id] = match.id;
                head.matched = true;
                match.matched = true;
            }
        });

        Deque<Node> remainGirl = Arrays.stream(girls).filter(x -> !x.matched).collect(Collectors.toCollection(() -> new ArrayDeque<>(n)));
        for (int i = 0; i < n; i++) {
            if (boys[i].matched) {
                continue;
            }
            arrange[i] = remainGirl.removeFirst().id;
        }

        for (int x : arrange) {
            out.append(x + 1).append(' ');
        }


    }


    Deque<Node> trace;

    public void dfs(Node root) {
        if (root == null) {
            return;
        }
        trace.addLast(root);
        dfs(root.adj);
    }
}


class Node {
    Node adj;
    int incoming;
    boolean boy;
    int id;
    Node p = this;
    int rank = 0;
    boolean matched;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
    }
}