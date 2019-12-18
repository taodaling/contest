package on2019_12.on2019_12_18_Codeforces_Global_Round_6.F__Almost_Same_Distance;




import template.datastructure.IntList;
import template.datastructure.MultiWayIntStack;
import template.graph.TreeDiameter;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FAlmostSameDistance {
    Node[] nodes;
    int[] ans;
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
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            type2(node, root);
        }

        if (p == null) {
            return;
        }

        int distInFather = maxDepthRegardless(root, p) + 1;
        int distOfFather = maxDepthRegardless(p, root) + 1;
        int encounter = 0;
        for (int i = root.dists.length - 1; i >= 0; i--) {
            int cur = root.dists[i];
            if (encounter == 0 && cur <= distOfFather) {
                encounter++;
            }
            int index = SequenceUtils.leftBound(p.dists, cur, 0, p.dists.length - 1);
            if (index < 0) {
                index = p.dists.length;
            } else if (cur <= distInFather) {
                index++;
            }
            int cnt = root.dists.length - i - encounter + p.dists.length - index;
            if (cnt > 1) {
                ans[2 * cur] = Math.max(ans[2 * cur], cnt);
            }
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
        root.dists = list.toArray();

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
    int[] dists;
    int id;

    static Comparator<Node> sortById = (a, b) -> a.id - b.id;

    @Override
    public String toString() {
        return "" + id;
    }
}

