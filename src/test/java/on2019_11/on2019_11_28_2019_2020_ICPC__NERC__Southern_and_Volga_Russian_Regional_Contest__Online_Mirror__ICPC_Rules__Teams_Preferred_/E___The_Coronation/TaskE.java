package on2019_11.on2019_11_28_2019_2020_ICPC__NERC__Southern_and_Volga_Russian_Regional_Contest__Online_Mirror__ICPC_Rules__Teams_Preferred_.E___The_Coronation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Node[][] nodes = new Node[n][2];
        BitOperator bo = new BitOperator();
        for (int i = 0; i < n; i++) {
            nodes[i][0] = new Node();
            nodes[i][0].id = i;
            nodes[i][1] = new Node();
            nodes[i][1].id = i;
            nodes[i][1].rev = 1;
            for (int j = 0; j < m; j++) {
                boolean one = in.readChar() == '1';
                nodes[i][0].set = bo.setBit(nodes[i][0].set, (m - 1 - j), one);
                nodes[i][1].set = bo.setBit(nodes[i][1].set, j, one);
            }
            addEdge(nodes[i][0], nodes[i][1]);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                for (int k1 = 0; k1 < 2; k1++) {
                    for (int k2 = 0; k2 < 2; k2++) {
                        if (m - Long.bitCount(nodes[i][k1].set ^ nodes[j][k2].set) < k) {
                            addEdge(nodes[i][k1], nodes[j][k2]);
                        }
                    }
                }
            }
        }

        boolean valid = true;
        List<Node> reverse = new ArrayList<>();
        for (Node[] pair : nodes) {
            Node node = pair[0];
            if (node.color != 0) {
                continue;
            }
            List<Node>[] lists = new List[]{
                    new ArrayList(), new ArrayList()
            };
            valid = valid && dfs(node, 1, lists);
            for (int i = 0; i < 2; i++) {
                lists[i] = lists[i].stream().filter(x -> x.rev == 1).collect(Collectors.toList());
            }
            if (lists[0].size() > lists[1].size()) {
                SequenceUtils.swap(lists, 0, 1);
            }
            reverse.addAll(lists[0]);
        }

        if (!valid) {
            out.println(-1);
            return;
        }
        out.println(reverse.size());
        for (Node node : reverse) {
            out.append(node.id + 1).append(' ');
        }
        out.println();
    }

    public void addEdge(Node a, Node b) {
        a.next.add(b);
        b.next.add(a);
    }

    public boolean dfs(Node root, int color, List<Node>[] lists) {
        if (root.color != 0) {
            return root.color == color;
        }
        root.color = color;
        lists[root.color - 1].add(root);
        for (Node node : root.next) {
            if (!dfs(node, 3 - color, lists)) {
                return false;
            }
        }
        return true;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int color;
    long set;
    int id;
    int rev;
}
