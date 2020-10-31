package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Course_Schedule;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 0; i < m; i++){
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            b.adj.add(a);
        }
        for(int i = 0; i < n; i++){
            if(dfs(nodes[i])){
                out.println("IMPOSSIBLE");
                return;
            }
        }
        for(Node node : seq){
            out.append(node.id + 1).append(' ');
        }
    }

    List<Node> seq = new ArrayList<>();

    public boolean dfs(Node root) {
        if (root.visited) {
            return root.instk;
        }
        root.visited = root.instk = true;
        for (Node node : root.adj) {
            if (dfs(node)) {
                return true;
            }
        }
        root.instk = false;
        seq.add(root);
        return false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    boolean visited;
    boolean instk;
}