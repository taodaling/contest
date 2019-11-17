package on2019_11.on2019_11_16_Codeforces_Round__600__Div__2_.D___Harmonious_Graph;



import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

import java.util.Arrays;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].l = nodes[i].r = i;
        }

        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[i][j] = in.readInt() - 1;
            }
        }

        for (int[] e : edges) {
            Node.merge(nodes[e[0]], nodes[e[1]]);
        }

        int cc = 0;
        for (int i = 0; i < n; i++) {
            if (nodes[i].find() == nodes[i]) {
                cc++;
            }
        }

        for (int i = 0; i < n; i++) {
            nodes[i].init();
        }

        for (int[] e : edges) {
            Node a = nodes[e[0]].find();
            Node b = nodes[e[1]].find();
            if (a.r > b.l) {
                Node tmp = a;
                a = b;
                b = tmp;
            }

            while (a.find().r < b.find().l) {
                Node next = nodes[a.find().r + 1].find();
                Node.merge(a, next);
                a.find().r = next.r;
            }
        }

        int curCC = 0;
        for (int i = 0; i < n; i++) {
            if (nodes[i].find() == nodes[i]) {
                curCC++;
            }
        }

        out.println(cc - curCC);
    }

    static class Node {
        Node p = this;
        int rank;

        public void init() {
            p = this;
            rank = 0;
        }

        int l;
        int r;

        public Node find() {
            return p.p == p ? p : (p = p.find());
        }

        public static void merge(Node a, Node b) {
            a = a.find();
            b = b.find();
            if (a == b) {
                return;
            }
            if (a.rank == b.rank) {
                a.rank++;
            }
            if (a.rank > b.rank) {
                b.p = a;
            } else {
                a.p = b;
            }
        }
    }
}

