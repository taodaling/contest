package on2020_04.on2020_04_18_AtCoder___AtCoder_Grand_Contest_043.C___Giant_Graph;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.List;

public class CGiantGraph {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[][] nodes = new Node[3][n];

        int sqrt = (int) Math.sqrt(2e5) + 1;
        int[][] sum = new int[3][sqrt + 1];

        int base = mod.valueOf((long) 1e18);
        CachedPow pow = new CachedPow(base, mod);


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].id = j + 1;
            }
            int m = in.readInt();
            for (int j = 0; j < m; j++) {
                Node a = nodes[i][in.readInt() - 1];
                Node b = nodes[i][in.readInt() - 1];
                if (a.id > b.id) {
                    Node tmp = a;
                    a = b;
                    b = tmp;
                }
                a.next.add(b);
            }

            for(int j = 0; j < n; j++) {
                dfsForSG(nodes[i][j]);
            }
            for (Node node : nodes[i]) {
                sum[i][node.sg] = mod.plus(sum[i][node.sg], pow.pow(node.id));
            }
        }

        int ans = 0;
        for (int i = 0; i <= sqrt; i++) {
            for (int j = 0; j <= sqrt; j++) {
                int k = i ^ j;
                if (k > sqrt) {
                    continue;
                }
                int local = mod.mul(sum[0][i], mod.mul(sum[1][j], sum[2][k]));
                ans = mod.plus(ans, local);
            }
        }

        out.println(ans);
    }

    IntegerVersionArray iva = new IntegerVersionArray(10000);

    public void dfsForSG(Node root) {
        if(root.visited){
            return;
        }
        root.visited = true;
        for (Node node : root.next) {
            dfsForSG(node);
        }
        iva.clear();
        for (Node node : root.next) {
            iva.set(node.sg, 1);
        }
        while (iva.get(root.sg) == 1) {
            root.sg++;
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int sg;
    int id;
    boolean visited;
}

