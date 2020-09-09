package on2020_09.on2020_09_07_Codeforces___Codeforces_Round__668__Div__1_.D__Game_of_Pairs0;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DGameOfPairs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n % 2 == 0) {
            int[] ans = new int[n * 2];
            for (int i = 0; i < 2 * n; i++) {
                ans[i] = i % n + 1;
            }
            out.println("First").flush();
            for (int i = 0; i < 2 * n; i++) {
                out.append(ans[i]).append(' ');
            }
            out.flush();
            in.readInt();
            return;
        }

        out.println("Second").flush();

        List<Integer>[] groups = IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[n]);
        for (int i = 0; i < n * 2; i++) {
            groups[in.readInt() - 1].add(i);
        }

        Node[] nodes = new Node[n * 2];
        for (int i = 0; i < n * 2; i++) {
            nodes[i] = new Node();
            nodes[i].id = i + 1;
        }

        for (List<Integer> e : groups) {
            Node a = nodes[e.get(0)];
            Node b = nodes[e.get(1)];
            a.r = b;
            b.r = a;
        }

        for (int i = 0; i < n; i++) {
            nodes[i].w = nodes[i + n];
            nodes[i + n].w = nodes[i];
        }

        for (Node node : nodes) {
            dfs(node);
        }

        int sum = 0;
        for (Node node : nodes) {
            if (node.select) {
                sum = (sum + node.id) % (2 * n);
            }
        }

        if (sum != 0) {
            for(Node node : nodes){
                node.select = !node.select;
            }
        }

        for(Node node : nodes){
            if(node.select){
                out.append(node.id).append(' ');
            }
        }

        out.flush();
        in.readInt();
    }

    public void dfs(Node root) {
        if (root.visit) {
            return;
        }
        root.visit = true;
        root.select = !root.w.select;
        dfs(root.w);
        dfs(root.r);
    }
}

class Node {
    int id;
    Node r;
    Node w;
    boolean select;
    boolean visit;
}


