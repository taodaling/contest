package on2020_12.on2020_12_29_Virtual_Judge___improve_contest_14.A___Control_function;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.*;

import java.util.ArrayList;
import java.util.List;

public class AControlFunction {
    IntegerObjectHashMap<Node> map = new IntegerObjectHashMap<>(2000, false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        IntegerArrayList list = new IntegerArrayList(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
                list.add(mat[i][j]);
            }
        }

        for (int i = 1; i < n; i++) {
            int index = -1;
            for (int j = 0; j < m; j++) {
                if (mat[i][j] != mat[0][j]) {
                    index = j;
                    break;
                }
            }
            Node a = get(mat[0][index]);
            Node b = get(mat[i][index]);
            if (a.t > b.t) {
                Node tmp = a;
                a = b;
                b = tmp;
            }
            b.adj.add(a);
        }

        list.unique();
        out.println("Yes");
        for (int x : list.toArray()) {
            out.append(x).append(" -> ");
            int to = 0;
            if (map.containKey(x)) {
                to = assign(map.get(x));
            }
            out.println(to);
        }
    }

    IntegerVersionArray iva = new IntegerVersionArray(100);

    public int assign(Node node) {
        if (node.assign == -1) {
            for (Node x : node.adj) {
                assign(x);
            }
            iva.clear();
            for (Node x : node.adj) {
                iva.set(assign(x), 1);
            }
            node.assign = 1;
            while (iva.get(node.assign) == 1) {
                node.assign++;
            }
        }
        return node.assign;
    }

    List<Node> nodes = new ArrayList<>(2000);

    public Node get(int i) {
        if (!map.containKey(i)) {
            Node node = new Node();
            node.t = i;
            map.put(i, node);
            nodes.add(node);
        }
        return map.get(i);
    }
}

class Node {
    int t;
    int assign = -1;
    List<Node> adj = new ArrayList<>();
}
