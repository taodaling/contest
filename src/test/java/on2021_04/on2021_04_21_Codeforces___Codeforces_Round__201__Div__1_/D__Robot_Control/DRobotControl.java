package on2021_04.on2021_04_21_Codeforces___Codeforces_Round__201__Div__1_.D__Robot_Control;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DRobotControl {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        int inf = (int)1e9;
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].dist = inf;
        }
        int m = in.ri();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.deg++;
            b.adj.add(a);
        }
        Node s = nodes[in.ri() - 1];
        Node t = nodes[in.ri() - 1];

        PriorityQueue<State> pq = new PriorityQueue<>(m + n, Comparator.comparingInt(x -> x.w));
        pq.add(new State(t, 0));
        while (!pq.isEmpty()) {
            State head = pq.remove();
            Node root = head.node;
            if (root.dist <= head.w) {
                continue;
            }
            root.dist = head.w;
            for(Node node : root.adj){
                node.deg--;
                if(node.deg == 0){
                    pq.add(new State(node, root.dist));
                }else{
                    pq.add(new State(node, root.dist + 1));
                }
            }
        }

        int ans = s.dist;
        if(ans == inf){
            out.println(-1);
        }else{
            out.println(ans);
        }
    }
}

class State {
    Node node;
    int w;

    public State(Node node, int w) {
        this.node = node;
        this.w = w;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int dist;
    int deg;
}
