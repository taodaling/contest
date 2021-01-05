package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DAcyclicOrganicCompounds {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].c = in.ri();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].v = in.rc() - 'a';
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        int max = 0;
        int cnt = 0;
        for(Node node : nodes){
            if(node.distinct + node.c > max){
                max = node.distinct + node.c;
                cnt = 0;
            }
            if(node.distinct + node.c == max){
                cnt++;
            }
        }
        out.append(max).append(' ').append(cnt);
    }

    public Trie merge(Trie a, Trie b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        for (int i = 0; i < a.next.length; i++) {
            a.size -= a.size(i);
            a.next[i] = merge(a.next[i], b.next[i]);
            a.size += a.size(i);
        }
        return a;
    }

    public Trie dfs(Node root, Node p) {
        Trie top = new Trie();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Trie res = dfs(node, root);
            top.size -= top.size(node.v);
            top.next[node.v] = merge(top.next[node.v], res);
            top.size += top.size(node.v);
        }
        root.distinct = top.size;
        return top;
    }


}

class Node {
    List<Node> adj = new ArrayList<>();
    int distinct;
    int c;
    int v;
}

class Trie {
    Trie[] next = new Trie['z' - 'a' + 1];
    int size = 1;

    int size(int i) {
        return next[i] == null ? 0 : next[i].size;
    }
}
