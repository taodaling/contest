package on2019_11.on2019_11_12_AtCoder_Grand_Contest_034.E___Complete_Compress0;



import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i <= n; i++) {
            nodes[i].pieces = in.readChar() - '0';
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }


        int inf = (int) 1e9;
        int ans = inf;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                nodes[j].combine = nodes[j].oneCnt = nodes[j].sumOfHeights = 0;
            }
            dfs(nodes[i], null);
            if (nodes[i].combine * 2 != nodes[i].sumOfHeights) {
                continue;
            }
            ans = Math.min(ans, nodes[i].combine);
        }

        out.println(ans == inf ? -1 : ans);
    }

    public void dfs(Node root, Node father) {
        int combineHappen = 0;
        int singleHeight = 0;

        for (Node node : root.next) {
            if (node == father) {
                continue;
            }
            dfs(node, root);
            node.sumOfHeights += node.oneCnt;
            root.oneCnt += node.oneCnt;
            root.sumOfHeights += node.sumOfHeights;

            int h = node.sumOfHeights - node.combine * 2;
            if (h <= singleHeight) {
                int extract = Math.min(node.combine, (singleHeight - h) / 2);
                node.combine -= extract;
                h += extract * 2;
                singleHeight -= h;
                combineHappen += h;
                combineHappen += node.combine;
            } else {
                int extract = Math.min((h - singleHeight) / 2, combineHappen);
                combineHappen -= extract;
                singleHeight += extract * 2;
                combineHappen += singleHeight;
                h -= singleHeight;
                singleHeight = h;
                combineHappen += node.combine;
            }
        }

        if (root.pieces == 1) {
            root.oneCnt++;
        }
        root.combine = combineHappen;
    }
}


class Node {
    List<Node> next = new ArrayList<>();
    int pieces;

    int combine;
    int sumOfHeights;
    int oneCnt;
}
