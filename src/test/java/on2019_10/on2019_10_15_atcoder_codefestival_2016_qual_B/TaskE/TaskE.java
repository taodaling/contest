package on2019_10.on2019_10_15_atcoder_codefestival_2016_qual_B.TaskE;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import template.FastInput;

public class TaskE {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        char[] s = new char[400000];
        Node root = new Node();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            int m = in.readString(s, 0);
            normalize(s, m);
            nodes[i] = add(root, s, 0, m);
        }

        int q = in.readInt();
        char[] perm = new char['z' - 'a' + 1];

        List<Query> queries = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            int k = in.readInt();
            in.readString(perm, 0);
            normalize(perm, perm.length);

            Query query = new Query();
            query.seq = perm.clone();
            nodes[k].queries.add(query);
            queries.add(query);
        }
        prepare(root, new int[Node.range][Node.range], 0);

        for(Query query : queries){
            out.println(query.ans);
        }
    }

    public void normalize(char[] s, int n) {
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
    }

    public Node add(Node node, char[] s, int i, int n) {
        node.word++;
        if (i == n) {
            node.val = 1;
            return node;
        }
        return add(node.get(s[i]), s, i + 1, n);
    }

    public void prepare(Node root, int[][] occurence, int prefix) {
        for (int i = 0; i < Node.range; i++) {
            if (root.next[i] == null) {
                continue;
            }
            for(int j = 0; j < Node.range; j++){
                if (root.next[j] == null) {
                    continue;
                }
                occurence[i][j] += root.next[j].word;
            }

            prepare(root.next[i], occurence, prefix + root.val);

            for(int j = 0; j < Node.range; j++){
                if (root.next[j] == null) {
                    continue;
                }
                occurence[i][j] -= root.next[j].word;
            }
        }

        for(Query q : root.queries){
            q.ans = 1 + prefix;
            for(int i = 1; i < Node.range; i++){
                for(int j = 0; j < i; j++){
                    q.ans += occurence[q.seq[i]][q.seq[j]];
                }
            }
        }
    }
}


class Query {
    char[] seq;
    int ans;
}


class Node {
    static int range = 'z' - 'a' + 1;

    int val;
    int word;
    Node father;
    char ch;

    Node[] next = new Node[range];

    List<Query> queries = new ArrayList<>();

    public Node get(int i) {
        if (next[i] == null) {
            next[i] = new Node();
            next[i].father = this;
            next[i].ch = (char) (i + 'a');
        }
        return next[i];
    }


    @Override
    public String toString() {
        return father == null ? "" : father.toString() + ch;
    }
}
