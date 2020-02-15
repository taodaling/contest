package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerDiscreteMap;
import template.primitve.generated.IntegerList;

public class FSessionInBSU {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] exams = new int[n][2];
        IntegerList list = new IntegerList(n * 2);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                exams[i][j] = in.readInt();
                list.add(exams[i][j]);
            }
        }
        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());
        Node[] nodes = new Node[dm.maxRank() + 1];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
        }
        for (int[] exam : exams) {
            int l = dm.rankOf(exam[0]);
            int r = dm.rankOf(exam[1]);
            if (nodes[l].find() == nodes[r].find()) {
                if (nodes[l].find().circle) {
                    out.println(-1);
                    return;
                }
                nodes[l].find().circle = true;
            } else {
                if (nodes[l].find().circle && nodes[r].find().circle) {
                    out.println(-1);
                    return;
                }
                Node.merge(nodes[l], nodes[r]);
            }
        }
        int tail = nodes.length - 1;
        while (tail >= 0 && nodes[tail].find().circle == false) {
            nodes[tail].find().circle = true;
            tail--;
        }

        out.println(dm.iThElement(tail));
    }
}

class Node {
    Node p = this;
    int rank = 0;
    boolean circle = false;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.circle = b.circle || a.circle;
    }
}