package on2020_06.on2020_06_11_Codeforces___Codeforces_Round__411__Div__1_.D__Expected_diameter_of_a_tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.*;

public class DExpectedDiameterOfATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (Node node : nodes) {
            if (node.top == null) {
                dfsForDiameter(node, null, 0, node);
                node.depthCnt = new int[node.size];
                node.suffix = new int[node.size];
                node.suffixSum = new long[node.size];
                findDepth(node, null, 0);
                for (int i = node.size - 1; i >= 0; i--) {
                    node.suffix[i] = node.depthCnt[i];
                    node.suffixSum[i] = (long) node.depthCnt[i] * i;
                    if (i + 1 < node.size) {
                        node.suffix[i] += node.suffix[i + 1];
                        node.suffixSum[i] += node.suffixSum[i + 1];
                    }
                }
            }
        }

        Map<Long, Double> cache = new HashMap<>(q);
        for (int i = 0; i < q; i++) {
            Node a = nodes[in.readInt() - 1].top;
            Node b = nodes[in.readInt() - 1].top;
            if (a == b) {
                out.println(-1);
                continue;
            }
            if (a.size > b.size || (a.size == b.size && a.id > b.id)) {
                Node tmp = a;
                a = b;
                b = tmp;
            }
            long key = DigitUtils.asLong(a.id, b.id);
            if (cache.containsKey(key)) {
                out.println(cache.get(key));
                continue;
            }
            long total = (long) a.size * b.size;
            long way = 0;
            long sum = 0;
            int diameter = Math.max(a.diameter, b.diameter);
            for (int j = 0; j < a.size; j++) {
                int k = Math.max(0, diameter + 1 - 1 - j);
                if (k >= b.size) {
                    continue;
                }
                way += b.suffix[k] * a.depthCnt[j];
                sum += a.depthCnt[j] * (b.suffixSum[k] + b.suffix[k] * (long) (j + 1));
            }

            sum += (total - way) * diameter;
            double ans = (double) sum / total;
            out.println(ans);
            cache.put(key, ans);
        }
    }

    public void dfsForDiameter(Node root, Node p, int depth, Node top) {
        root.depth = depth;
        root.farthest = root.depth;
        root.top = top;
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDiameter(node, root, depth + 1, top);
            root.size += node.size;
            root.diameter = Math.max(root.diameter, node.diameter);
            root.diameter = Math.max(root.diameter, root.farthest + node.farthest - 2 * root.depth);
            root.farthest = Math.max(root.farthest, node.farthest);
        }
    }

    public void findDepth(Node root, Node p, int height) {
        root.height = Math.max(height, root.farthest - root.depth);
        root.top.depthCnt[root.height]++;

        int[] max = new int[2];
        Arrays.fill(max, root.depth);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (max[0] >= node.farthest) {
                continue;
            }
            max[0] = node.farthest;
            if (max[0] > max[1]) {
                SequenceUtils.swap(max, 0, 1);
            }
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            int h = height;
            int cand = max[1] == node.farthest ? max[0] : max[1];
            h = Math.max(h, cand - root.depth) + 1;
            findDepth(node, root, h);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Node top;
    int depth;
    int id;
    int height;
    int size;
    int[] depthCnt;
    int[] suffix;
    long[] suffixSum;

    int diameter;
    int farthest;
}
