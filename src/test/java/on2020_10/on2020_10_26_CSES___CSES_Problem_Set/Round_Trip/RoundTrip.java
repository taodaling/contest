package on2020_10.on2020_10_26_CSES___CSES_Problem_Set.Round_Trip;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RoundTrip {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 0; i < m; i++){
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            Edge e = new Edge(u, v);
            u.adj.add(e);
            v.adj.add(e);
        }

        for(Node node : nodes){
            if(node.visited){
                continue;
            }
            if(dfs(node, null)){
                out.println(dq.size());
                for(Node x : dq){
                    out.append(x.id + 1).append(' ');
                }
                return;
            }
        }
        out.println("IMPOSSIBLE");
    }

    Deque<Node> dq = new ArrayDeque<>();
    public boolean dfs(Node root, Edge p){
        if(root.visited){
            while(dq.peekFirst() != root){
                dq.removeFirst();
            }
            dq.addLast(root);
            return true;
        }
        root.visited = true;
        dq.addLast(root);
        for(Edge e : root.adj){
            if(e == p){
                continue;
            }
            if(dfs(e.other(root), e)){
                return true;
            }
        }
        dq.removeLast();
        return false;
    }
}

class Edge{
    Node a;
    Node b;
    Node other(Node x){
        return x == a ? b : a;
    }

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
    }
}

class Node{
    boolean visited;
    List<Edge> adj = new ArrayList<>();
    int id;
}
