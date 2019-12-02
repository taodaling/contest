package on2019_12.on2019_12_02_Codeforces_Round__582__Div__3_.F___Unstable_String_Sort;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] p = new int[n];
        int[] q = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }
        for (int i = 0; i < n; i++) {
            q[i] = in.readInt() - 1;
        }
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            nodes[q[i]].next.add(nodes[q[i - 1]]);
            nodes[p[i]].next.add(nodes[p[i - 1]]);
        }

        int distinct = 0;
        for (Node node : nodes) {
            tarjan(node);
            if (node == node.set) {
                distinct++;
            }
        }

        if (distinct < k) {
            out.println("NO");
            return;
        }

        List<Node> trace = new ArrayList<>(n);
        for (Node node : nodes) {
            topologicalSort(node, trace);
        }
        for (int i = 0; i < trace.size(); i++) {
            trace.get(i).val = Math.min(i, 25);
        }
        out.println("YES");
        for(Node node : nodes){
            out.append((char)(node.set.val + 'a'));
        }
    }

    public void topologicalSort(Node root, List<Node> trace) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        for (Node node : root.next) {
            topologicalSort(node, trace);
        }
        if (root.set == root) {
            trace.add(root);
        }
    }

    Deque<Node> deque = new ArrayDeque<>(200000);
    int dfn = 1;

    public void tarjan(Node root) {
        if (root.dfn != 0) {
            return;
        }
        root.dfn = root.low = dfn++;
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
                Node tail = deque.removeLast();
                tail.set = root;
                tail.instk = false;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>(2);
    Node set;
    int dfn;
    int low;
    boolean instk;
    int val;
    boolean visited;
}