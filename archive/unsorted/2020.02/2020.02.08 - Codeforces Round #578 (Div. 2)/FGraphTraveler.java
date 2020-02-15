package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerVersionArray;

import java.util.ArrayDeque;
import java.util.Deque;

public class FGraphTraveler {
    static int lcm = 8 * 9 * 7 * 5;
    static Modular mod = new Modular(lcm);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] tags = new int[n];
        for (int i = 0; i < n; i++) {
            tags[i] = mod.valueOf(in.readInt());
        }
        int[][] edges = new int[n][];
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            edges[i] = new int[k];
            for (int j = 0; j < k; j++) {
                edges[i][j] = in.readInt() - 1;
            }
        }
        Node[][] nodes = new Node[n][lcm];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < lcm; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].id = i;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < lcm; j++) {
                int val = mod.plus(j, tags[i]);
                nodes[i][j].next = nodes[edges[i][val % edges[i].length]][val];
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int x = in.readInt() - 1;
            int y = mod.valueOf(in.readInt());
            int ans = dp(nodes[x][y]);
            out.println(ans);
        }
    }

    Deque<Node> deque = new ArrayDeque<>(lcm * 1000);
    IntegerVersionArray iva = new IntegerVersionArray(1000);

    public int dp(Node root) {
        if (root.cnt == -2) {
            //find a loop
            iva.clear();
            root.cnt = 0;
            while (true) {
                Node last = deque.removeLast();
                last.instk = false;
                if (iva.get(last.id) == 0) {
                    iva.set(last.id, 1);
                    root.cnt++;
                }
                if (last == root) {
                    break;
                }
            }
            return root.cnt;
        }
        if (root.cnt != -1) {
            return root.cnt;
        }

        deque.addLast(root);
        root.cnt = -2;
        root.cnt = dp(root.next);
        if (root.instk) {
            deque.removeLast();
            root.instk = false;
        }
        return root.cnt;
    }
}

class Node {
    Node next;
    int cnt = -1;
    int id;
    boolean instk;
}