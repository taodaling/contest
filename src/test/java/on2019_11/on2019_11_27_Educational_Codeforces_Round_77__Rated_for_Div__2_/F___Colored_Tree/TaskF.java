package on2019_11.on2019_11_27_Educational_Codeforces_Round_77__Rated_for_Div__2_.F___Colored_Tree;





import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }
        Edge[] edges = new Edge[n];

    }
}

class Edge {
    Node a;
    Node b;
    int top;
    int bot;
}

class Node {
    List<Edge> next = new ArrayList<>();
}