package on2019_10.on2019_10_14_.TaskE;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import template.FastInput;

public class TaskE {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();

        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }

        Node x = nodes[in.readInt()];
        Node y = nodes[in.readInt()];


        for(int i = 1; i < n; i++){
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.red.add(b);
            b.red.add(a);
        }

        for(int i = 1; i < n; i++){
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.blue.add(b);
            b.blue.add(a);
        }

        dfsForDepth(y, null, 0);
    }

    public void dfsForDepth(Node root, Node fa, int depth){
        root.blue.remove(fa);
        root.depth = depth;
        for(Node node : root.blue){
            dfsForDepth(node, root, depth + 1);
        }
    }


}


class Node {
    List<Node> red = new ArrayList<>(2);
    List<Node> blue = new ArrayList<>(2);
    boolean escape;
    int depth;
}
