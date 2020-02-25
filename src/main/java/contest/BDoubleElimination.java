package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BDoubleElimination {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        if (k == 0) {
            out.println(0);
            return;
        }
//        if (k == 1) {
//            out.println(n * 2 + 2);
//            return;
//        }

        Node[] replacement = new Node[1 << (n - 1)];
        for (int i = 0; i < replacement.length; i++) {
            replacement[i] = new Node();
        }
        Node[] nodes = new Node[1 << n];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
            nodes[i].replacement = replacement[i / 2];
        }

        merge0(nodes);
        merge1(replacement);
        int sum = 1 + k;
        for (int i = 0; i < k; i++) {
            int id = in.readInt() - 1;
            nodes[id].repr = nodes[id];
        }

        for (int i = 0; i < nodes.length; i += 2) {
            if (nodes[i].repr != null && nodes[i + 1].repr != null) {
                sum += update(nodes[i].replacement);
                nodes[i].repr = null;
            }
        }

        Node[] parents = getP(nodes);
        sum += dpOnTree(parents, 0);
        out.println(sum);
    }

    public Node[] getP(Node[] nodes) {
        Node[] parents = new Node[nodes.length / 2];
        for (int i = 0; i < nodes.length; i += 2) {
            parents[i / 2] = nodes[i].parent;
        }
        return parents;
    }

    public int dpOnTree(Node[] nodes, int level) {
        int ans = 0;
        for (Node node : nodes) {
            if (node.children[0].repr != null &&
                    node.children[1].repr != null) {
                if (test(node.children[0].repr.replacement) <
                        test(node.children[1].repr.replacement)) {
                    SequenceUtils.swap(node.children, 0, 1);
                }
                if(test(node.children[0].repr.replacement) > level){
                    ans += update(node.children[0].repr.replacement) - level;
                    node.children[0].repr = null;
                }
            }
            for (int i = 0; i < 2; i++) {
                if (node.children[i].repr != null) {
                    node.repr = node.children[i].repr;
                }
            }
        }

        if (nodes.length == 1) {
            if (test(nodes[0].repr.replacement) > level + 1) {
                ans += update(nodes[0].repr.replacement) - level;
            }
        } else {

            ans += dpOnTree(getP(nodes), level + 1);
        }
        return ans;
    }

    public void merge0(Node[] nodes) {
        if (nodes.length == 1) {
            return;
        }
        Node[] parents = new Node[nodes.length / 2];
        for (int i = 0; i < parents.length; i++) {
            parents[i] = new Node();
        }
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].parent = parents[i / 2];
            parents[i / 2].children[i % 2] = nodes[i];
        }
        merge0(parents);
    }

    public void merge1(Node[] nodes) {
        Node[] tmp = new Node[nodes.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = new Node();
            nodes[i].parent = tmp[i];
        }
        if (nodes.length == 1) {
            return;
        }
        Node[] parents = new Node[nodes.length / 2];
        for (int i = 0; i < parents.length; i++) {
            parents[i] = new Node();
        }
        for (int i = 0; i < nodes.length; i++) {
            tmp[i].parent = parents[i / 2];
            parents[i / 2].children[i % 2] = tmp[i];
        }
        merge1(parents);

    }

    public int test(Node node){
        return test0(node);// - 1;
    }

    public int test0(Node node) {
        if (node == null || node.paint) {
            return 0;
        }
        //node.paint = true;
        return 1 + test0(node.parent);
    }

    public int update(Node node){
        return update0(node);// - 1;
    }

    public int update0(Node node) {
        if (node == null || node.paint) {
            return 0;
        }
        node.paint = true;
        return 1 + update0(node.parent);
    }

    public int dist(int i, int j) {
        int height = Bits.theFirstDifferentIndex(i, j);
        return height * 2;
    }
}

class Node {
    Node parent;
    Node[] children = new Node[2];
    boolean paint;
    Node replacement;
    Node repr;
}