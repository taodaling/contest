package contest;

import template.datastructure.IntList;
import template.datastructure.MultiWayIntStack;
import template.graph.TreeDiameter;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FAlmostSameDistance {
    Node[] nodes;
    int[] ans;
    Segment segment;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        ans = new int[n + 1];
        nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        MultiWayIntStack edges = new MultiWayIntStack(n + 1, n * 2);
        for (int i = 1; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            nodes[a].next.add(nodes[b]);
            nodes[b].next.add(nodes[a]);
            edges.addLast(a - 1, b - 1);
            edges.addLast(b - 1, a - 1);
        }
        TreeDiameter diameter = new TreeDiameter(edges, n);
        ans[diameter.getDiameter()] = 2;
        ans[n] = 1;

        for (int i = 1; i <= n; i++) {
            nodes[i].next.sort(Node.sortById);
        }

        segment = new Segment(0, 2 * n);
        dfsForMaxDepth(nodes[1], null, 0);
        prepareForMax(nodes[1], null, 0);
        type1(nodes[1], null);
        type2(nodes[1], null);
        for (int i = n - 1; i >= 1; i--) {
            ans[i] = Math.max(ans[i], ans[i + 1]);
        }

        for (int i = 1; i <= n; i++) {
            out.println(ans[i]);
        }
    }

    IntList list = new IntList(500000);

    public void type2(Node root, Node p) {
        list.clear();
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            list.add(node.maxDepth - root.depth);
        }
        ans[1] = Math.max(ans[1], 2);
        list.sort();
        int m = list.size();
        for (int i = m - 1; i >= 0; i--) {
            int cur = list.get(i);
            ans[cur * 2] = Math.max(ans[cur * 2], segment.query(cur + n - root.depth, 2 * n, 0, 2 * n) + m - i);
        }
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            int x = node.maxDepth - root.depth + n - root.depth;
            segment.update(x, x, 0, 2 * n, 1);
        }

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            int x = node.maxDepth - root.depth + n - root.depth;
            segment.update(x, x, 0, 2 * n, -1);
            type2(node, root);
            segment.update(x, x, 0, 2 * n, 1);
        }

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            int x = node.maxDepth - root.depth + n - root.depth;
            segment.update(x, x, 0, 2 * n, -1);
        }
    }

    public void type1(Node root, Node p) {
        int m = root.next.size();
        list.clear();
        for (Node node : root.next) {
            int depth = maxDepthRegardless(node, root) + 1;
            list.add(depth);
        }
        list.sort();
        for (int i = m - 1; i >= 0; i--) {
            int cur = list.get(i);
            int cnt = m - i;
            if (cnt > 1) {
                ans[cur * 2] = Math.max(ans[cur * 2], cnt);
            }
        }

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            type1(node, root);
        }

        ans[1] = Math.max(ans[1], m + 1);
    }

    public static int maxDepthRegardless(Node root, Node node) {
        int index = Collections.binarySearch(root.next, node, Node.sortById);
        return maxRegardless(root, index);
    }

    public static void prepareForMax(Node root, Node p, int maxDepth) {
        int m = root.next.size();
        root.preMax = new int[m];
        root.postMax = new int[m];
        for (int i = 0; i < m; i++) {
            Node node = root.next.get(i);
            root.preMax[i] = node == p ? maxDepth : (node.maxDepth - root.depth);
            if (i > 0) {
                root.preMax[i] = Math.max(root.preMax[i], root.preMax[i - 1]);
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            Node node = root.next.get(i);
            root.postMax[i] = node == p ? maxDepth : (node.maxDepth - root.depth);
            if (i + 1 < m) {
                root.postMax[i] = Math.max(root.postMax[i], root.postMax[i + 1]);
            }
        }

        for (int i = 0; i < m; i++) {
            Node node = root.next.get(i);
            if (node == p) {
                continue;
            }
            prepareForMax(node, root, maxRegardless(root, i) + 1);
        }
    }

    public static int maxRegardless(Node root, int i) {
        int max = 0;
        if (i > 0) {
            max = Math.max(max, root.preMax[i - 1]);
        }
        if (i + 1 < root.next.size()) {
            max = Math.max(max, root.postMax[i + 1]);
        }
        return max;
    }

    public static void dfsForMaxDepth(Node root, Node p, int depth) {
        root.depth = root.maxDepth = depth;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForMaxDepth(node, root, depth + 1);
            root.maxDepth = Math.max(root.maxDepth, node.maxDepth);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int[] preMax;
    int[] postMax;
    int maxDepth;
    int depth;
    int id;

    static Comparator<Node> sortById = (a, b) -> a.id - b.id;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int cnt;

    public void pushUp() {
        cnt = left.cnt + right.cnt;
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            cnt += x;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return cnt;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }
}
