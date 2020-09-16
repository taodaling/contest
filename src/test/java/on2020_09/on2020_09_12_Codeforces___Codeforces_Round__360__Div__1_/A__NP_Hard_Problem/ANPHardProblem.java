package on2020_09.on2020_09_12_Codeforces___Codeforces_Round__360__Div__1_.A__NP_Hard_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ANPHardProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (Node node : nodes) {
            if (node.color == -1) {
                dfs(node, 0);
            }
        }

        if (conflict) {
            out.println(-1);
            return;
        }

        IntegerArrayList[] lists = IntStream.range(0, 2).mapToObj(i -> new IntegerArrayList()).toArray(i -> new IntegerArrayList[i]);
        for (int i = 0; i < n; i++) {
            lists[nodes[i].color].add(i + 1);
        }
        for(IntegerArrayList list : lists){
            out.println(list.size());
            for(int i = 0; i < list.size(); i++){
                out.append(list.get(i)).append(' ');
            }
            out.println();
        }
    }

    boolean conflict = false;

    public void dfs(Node root, int exp) {
        if (root.color != -1) {
            if (root.color != exp) {
                conflict = true;
            }
            return;
        }
        root.color = exp;
        for (Node node : root.adj) {
            dfs(node, exp ^ 1);
        }
    }
}

class Node {
    int color = -1;
    List<Node> adj = new ArrayList<>();
}
