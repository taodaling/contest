package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectPointSumProblem;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class PrefixesAndSuffixes {
    char[] s = new char[(int) 2e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] pref = new Node[n];
        Node[] suf = new Node[n];
        Node root = new Node();
        for (int i = 0; i < n; i++) {
            int len = in.rs(s, 0);
            pref[i] = build(root, len);
            SequenceUtils.reverse(s, 0, len - 1);
            suf[i] = build(root, len);
        }
        for (int i = Node.allNodes.size() - 1; i >= 0; i--) {
            Node node = Node.allNodes.get(i);
            node.size++;
            if (node.fa != null) {
                node.fa.size += node.size;
            }
        }
        for (Node node : Node.allNodes) {
            if (node.fa != null) {
                node.close = node.open = node.fa.close + 1;
                node.fa.close += node.size;
            }
        }
        RectPointSumProblem.Point2D[] pts = new RectPointSumProblem.Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new RectPointSumProblem.Point2D(pref[i].open, suf[i].open, 1);
        }
        int m = in.ri();
        RectPointSumProblem.Query2D[] qs = new RectPointSumProblem.Query2D[m];
        Node dummy = new Node();
        dummy.open = -1;
        dummy.close = -1;
        for (int i = 0; i < m; i++) {

            int len = in.rs(s, 0);
            Node a = find(root, len);
            if (a == null) {
                a = dummy;
            }
            len = in.rs(s, 0);
            SequenceUtils.reverse(s, 0, len - 1);
            Node b = find(root, len);
            if (b == null) {
                b = dummy;
            }
            qs[i] = new RectPointSumProblem.Query2D(a.open, a.close, b.open, b.close);
        }
        long[] ans = RectPointSumProblem.solve(pts, qs);
        for(long x : ans){
            out.println(x);
        }
    }

    public Node build(Node root, int n) {
        for (int i = 0; i < n; i++) {
            root = root.get(s[i] - 'a');
        }
        return root;
    }

    public Node find(Node root, int n) {
        for (int i = 0; i < n && root != null; i++) {
            root = root.next[s[i] - 'a'];
        }
        return root;
    }

}


class Node {
    Node[] next = new Node['z' - 'a' + 1];
    Node fa;
    int open;
    int close;
    int size;
    static List<Node> allNodes = new ArrayList<>((int) 1e5 + 1);

    public Node() {
        allNodes.add(this);
    }

    public Node get(int i) {
        if (next[i] == null) {
            next[i] = new Node();
            next[i].fa = this;
        }
        return next[i];
    }
}
