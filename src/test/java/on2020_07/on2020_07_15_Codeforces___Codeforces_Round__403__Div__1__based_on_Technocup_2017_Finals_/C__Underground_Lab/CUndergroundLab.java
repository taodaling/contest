package on2020_07.on2020_07_15_Codeforces___Codeforces_Round__403__Div__1__based_on_Technocup_2017_Finals_.C__Underground_Lab;



import dp.Lis;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class CUndergroundLab {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        int m = in.readInt();
        int k = in.readInt();
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            if (dsu.find(a.id) == dsu.find(b.id)) {
                continue;
            }
            dsu.merge(a.id, b.id);
            a.adj.add(b);
            b.adj.add(a);
        }

        List<Node> trace = new ArrayList<>(2 * n);
        dfs(nodes[0], null, trace);
        List<Node>[] ans = new List[k];
        int threshold = DigitUtils.ceilDiv(2 * n, k);
        for(int i = 0; i < k; i++){
            ans[i] = new ArrayList(threshold);
        }
        int cur = 0;
        for(Node node : trace){
            while(ans[cur].size() >= threshold){
                cur++;
            }
            ans[cur].add(node);
        }
        for(int i = 0; i < k; i++){
            if(ans[i].size() == 0){
                ans[i].add(nodes[0]);
            }
            out.append(ans[i].size()).append(' ');
            for(Node node : ans[i]){
                out.append(node.id + 1).append(' ');
            }
            out.println();
        }
    }

    public void dfs(Node root, Node p, List<Node> trace) {
        trace.add(root);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, trace);
            trace.add(root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
}
