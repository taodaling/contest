package contest;

import template.algo.BinarySearch;
import template.datastructure.FixedMinCollection;
import template.datastructure.FixedMinHeap;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerFixedMinHeap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;

public class EPreorderTest {

    private void dfs1(Node root, Node p) {
        root.first = root.second = null;
        root.sum = root.v;
        root.totalProvide = 0;
        root.dp = 0;
        root.heap.clear();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs1(node, root);
            root.sum += node.sum;
            if (node.sum == 0) {
                root.totalProvide += node.dp;
            } else {
                root.heap.add(node.dp);
            }
        }
        if (root.v == 1) {
            root.dp = 0;
        } else {
            root.dp = root.totalProvide + 1;
            if (root.heap.size() > 0) {
                root.dp += root.heap.getKthSmallest(0);
            }
        }
    }

    private void dfs2(Node root, Node p, int topSum, int topDp) {
        if (topSum == 0) {
            root.totalProvide += topDp;
        } else {
            root.heap.add(topDp);
        }
        root.sum += topSum;
        if (root.v == 1) {
            root.best = 0;
        } else {
            root.best = root.totalProvide + 1;
            if (root.heap.size() > 0) {
                root.best += root.heap.getKthSmallest(0);
            }
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            int sumForChild = root.sum - node.sum;
            int dpForChild = 0;
            if (root.v == 0) {
                dpForChild += root.totalProvide + 1;
                if (node.sum == 0) {
                    dpForChild -= node.dp;
                }
                if (root.heap.size() > 0) {
                    if (root.heap.getKthSmallest(0) != node.dp) {
                        dpForChild += root.heap.getKthSmallest(0);
                    } else if (root.heap.size() > 1) {
                        dpForChild += root.heap.getKthSmallest(1);
                    }
                }
            }
            dfs2(node, root, sumForChild, dpForChild);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 0; i < n - 1; i++){
            Node u = nodes[in.ri() - 1];
            Node v = nodes[in.ri() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }
        IntPredicate predicate = mid -> {
            for(int i = 0; i < n; i++){
                nodes[i].v = a[i] >= mid ? 0 : 1;
            }
            dfs1(nodes[0], null);
            dfs2(nodes[0], null, 0, 0);
            for(Node node : nodes){
                if(node.best >= k){
                    return true;
                }
            }
            return false;
        };

        int ans = BinarySearch.lastTrue(predicate, 0, (int)1e6);
        out.println(ans);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Node first;
    Node second;
    IntegerFixedMinHeap heap = new IntegerFixedMinHeap(2, IntegerComparator.REVERSE_ORDER);
    int totalProvide;
    int sum;
    int v;
    int dp;

    int best;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
