package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.math.DigitUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CMuseumsTour {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int d = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].leave = new int[d];
            nodes[i].dp = new int[d];
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.prev.add(a);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                nodes[i].open = Bits.setBit(nodes[i].open, j, in.readChar() == '1');
            }
        }

        for (Node node : nodes) {
            tarjan(node);
        }

        for (Node node : nodes) {
            if (node.set == node) {
                node.accessible = 1;
                deque.addLast(node);
                node.inque = true;
                node.distFromSet = 0;
            }
        }

        long mask = (1L << d) - 1;
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            head.inque = false;
            long prov = head.accessible << 1;
            prov = Bits.swapBit(prov, d, 0);
            for (Node node : head.next) {
                if (node.set != head.set) {
                    continue;
                }
                if ((node.accessible | prov) == node.accessible) {
                    continue;
                }
                node.distFromSet = head.distFromSet + 1;
                node.accessible = node.accessible | prov;
                if (node.inque) {
                    continue;
                }
                node.inque = true;
                deque.addLast(node);
            }
        }

        for (Node node : nodes) {
            if (node.set == node) {
                //dfsForPeriod(node, 0, 0);
                dfsForDistToSet(node, 0);
                countForPeriod(node);
            }
        }


        int ans = queryOn(nodes[0], 0);
        out.println(ans);
    }


    public static void dfsForDistToSet(Node root, int dist) {
        if (root.distToSet != -1) {
            return;
        }
        root.distToSet = dist;
        for (Node node : root.prev) {
            if (node.set != root.set) {
                continue;
            }
            dfsForDistToSet(node, dist + 1);
        }
    }

    public static int queryOn(Node root, int i) {
        dfsForDp(root);
        i = (i + root.distToSet) % root.set.period;
        return root.set.dp[i];
    }

    public static void countForPeriod(Node root) {
        root.period = 1;
        while (0 == Bits.bitAt(root.accessible, root.period % root.dp.length)) {
            root.period++;
        }
    }

    public static void dfsForDp(Node root) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        int d = root.dp.length;
        for (int i = 0; i < root.set.period; i++) {
            boolean possible = false;
            for (int j = i; j < d; j += root.set.period) {
                possible = possible || Bits.bitAt(root.open, j) == 1;
            }
            if (possible) {
                root.set.dp[DigitUtils.mod(i - root.distFromSet, root.set.period)]++;
            }
        }
        for (Node node : root.next) {
            if (node.set == root.set) {
                dfsForDp(node);
            } else {
                for (int i = 0; i < d; i++) {
                    int index = DigitUtils.mod(i - root.distFromSet, d);
                    root.set.leave[index] = Math.max(root.set.leave[index], queryOn(node, i + 1));
                }
            }
        }


        if (root.set == root) {
            for (int i = 0; i < root.period; i++) {
                int mx = 0;
                for (int j = i; j < d; j += root.period) {
                    mx = Math.max(mx, root.leave[j]);
                }
                root.dp[i] += mx;
            }
        }

    }

    Deque<Node> deque = new ArrayDeque<>(100000);
    int dfn;

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = ++dfn;
        root.instk = true;
        deque.addLast(root);
        for (Node node : root.next) {
            tarjan(node);
            if (node.instk && node.low < root.low) {
                root.low = node.low;
            }
        }

        if (root.low == root.dfn) {
            while (true) {
                Node head = deque.removeLast();
                head.set = root;
                head.instk = false;
                if (head == root) {
                    break;
                }
            }
        }
    }
}

class Node {
    long accessible;
    int[] leave;
    int[] dp;
    int period;
    List<Node> next = new ArrayList<>();
    List<Node> prev = new ArrayList<>();
    int id;
    Node set;
    int dfn;
    int low;
    boolean instk;
    long open;
    int distToSet = -1;
    int distFromSet;
    boolean visited;
    boolean inque;

    @Override
    public String toString() {
        return "" + id;
    }
}
