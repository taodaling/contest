package contest;

import java.util.ArrayList;
import java.util.List;

import template.CompareUtils;
import template.FastInput;
import template.FastOutput;
import template.IntList;
import template.SequenceUtils;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        if (n == 2) {
            out.println("1 2");
            return;
        }

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        for (int i = 1; i <= n; i++) {
            nodes[i].isLeaf = nodes[i].next.size() == 1;
        }


        boolean valid = true;
        for (int i = 1; i <= n; i++) {
            for (Node node : nodes[i].next) {
                if (!node.isLeaf) {
                    nodes[i].end++;
                }
            }
            nodes[i].tag = !nodes[i].isLeaf && nodes[i].end <= 2;
            valid = valid && nodes[i].end <= 2;
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        List<Node> trace = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            if (nodes[i].tag && nodes[i].end <= 1) {
                dfs(nodes[i], null, trace);
                break;
            }
        }

        for (Node node : trace.get(0).next) {
            if (node.isLeaf) {
                trace.get(0).childrenNum--;
                trace.add(0, node);
                node.selected = true;
                break;
            }
        }

        for (Node node : trace.get(trace.size() - 1).next) {
            if (node.isLeaf && !node.selected) {
                trace.get(trace.size() - 1).childrenNum--;
                trace.add(node);
                node.selected = true;
                break;
            }
        }

        IntList p1 = new IntList(n);
        IntList p2 = new IntList(n);

        genPerm(trace, p1);
        SequenceUtils.reverse(trace, 0, trace.size() - 1);
        genPerm(trace, p2);

        if (CompareUtils.compareArray(p1.getData(), p2.getData(), 0, p1.size() - 1, 0, p2.size() - 1) > 0) {
            IntList tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        for (int i = 0; i < n; i++) {
            out.append(p1.get(i)).append(' ');
        }
    }

    public void genPerm(List<Node> nodes, IntList perm) {
        for (Node node : nodes) {
            int val = perm.size() + 1;
            for (int i = 1; i <= node.childrenNum; i++) {
                perm.add(val + i);
            }
            perm.add(val);
        }
    }


    public void dfs(Node root, Node father, List<Node> trace) {
        trace.add(root);
        root.childrenNum = root.next.size() - root.end;
        root.selected = true;
        for (Node node : root.next) {
            if (node == father || !node.tag) {
                continue;
            }
            dfs(node, root, trace);
            return;
        }
    }

}


class Node {
    List<Node> next = new ArrayList<>();
    boolean tag;
    boolean isLeaf;
    int end;
    boolean selected;
    int childrenNum;
}
