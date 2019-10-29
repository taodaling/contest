package on2019_10.on2019_10_28_338679.TaskC;



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.TwoSat;
import template.VersionArray;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i <= n; i++) {
            Node p = nodes[in.readInt()];
            p.next.add(nodes[i]);
        }

        for (int i = 1; i <= n; i++) {
            if (detectCircle(nodes[i])) {
                break;
            }
        }

        if (circle.size() == 0) {
            possible(out);
            return;
        }

        // remove any edge and add back later
        circle.get(0).next.remove(circle.get(1));
        for (int i = 1; i <= n; i++) {
            dfsForMex(nodes[i]);
        }
        circle.get(0).next.add(circle.get(1));

        for (Node node : circle) {
            va.clear();
            for (Node next : node.next) {
                if (next.incircle) {
                    continue;
                }
                va.set(next.mex, 1);
            }
            for (int i = 0;; i++) {
                if (va.get(i) == 0) {
                    if (node.choice1 == -1) {
                        node.choice1 = i;
                    } else {
                        node.choice2 = i;
                        break;
                    }
                }
            }
        }

        int m = circle.size();
        TwoSat twoSat = new TwoSat(m);
        for (int i = 0; i < m; i++) {
            Node node = circle.get(i);
            Node next = circle.get((i + 1) % m);
            int id = i + 1;
            int nid = i + 2;
            if (nid == m + 1) {
                nid = 1;
            }
            if (node.choice1 == next.choice1) {
                twoSat.deduce(twoSat.getElement(nid), twoSat.getNotElement(id));
            } else {
                twoSat.deduce(twoSat.getElement(nid), twoSat.getElement(id));
            }
            if (node.choice1 == next.choice2) {
                twoSat.deduce(twoSat.getNotElement(nid), twoSat.getNotElement(id));
            } else {
                twoSat.deduce(twoSat.getNotElement(nid), twoSat.getElement(id));
            }
        }

        boolean ans = twoSat.solve(false);
        if (ans) {
            possible(out);
        } else {
            impossible(out);
        }
    }

    public void possible(FastOutput out) {
        out.println("POSSIBLE");
        return;
    }

    public void impossible(FastOutput out) {
        out.println("IMPOSSIBLE");
        return;
    }


    Deque<Node> deque = new ArrayDeque<>(200000);
    List<Node> circle = new ArrayList<>(200000);

    public boolean detectCircle(Node root) {
        if (root.visited) {
            if (!root.instk) {
                return false;
            }
            // find a loop
            while (deque.peekFirst() != root) {
                deque.removeFirst();
            }
            circle.addAll(deque);
            for (Node node : circle) {
                node.incircle = true;
            }
            return true;
        }
        root.visited = root.instk = true;
        deque.addLast(root);

        for (Node node : root.next) {
            if (detectCircle(node)) {
                return true;
            }
        }

        deque.removeLast();
        root.instk = false;
        return false;
    }

    VersionArray va = new VersionArray(300000);

    public void dfsForMex(Node root) {
        if (root.mex != -1) {
            return;
        }
        for (Node node : root.next) {
            dfsForMex(node);
        }
        va.clear();
        for (Node node : root.next) {
            va.set(node.mex, 1);
        }
        for (int i = 0;; i++) {
            if (va.get(i) == 0) {
                root.mex = i;
                break;
            }
        }
    }
}


class Node {
    List<Node> next = new ArrayList<>();
    int id;
    boolean incircle;
    boolean visited;
    boolean instk;
    int mex = -1;

    // true
    int choice1 = -1;
    // false
    int choice2 = -1;
}
