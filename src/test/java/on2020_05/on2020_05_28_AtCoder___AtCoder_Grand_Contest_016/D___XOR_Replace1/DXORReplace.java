package on2020_05.on2020_05_28_AtCoder___AtCoder_Grand_Contest_016.D___XOR_Replace1;




import template.datastructure.DiscreteMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class DXORReplace {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        int[] b = new int[n + 1];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt();
        }
        a[n] = xor(a, 0, n - 1);
        b[n] = xor(b, 0, n - 1);

        IntegerList list = new IntegerList(n + n + 2);
        list.addAll(a);
        list.addAll(b);
        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());
        for (int i = 0; i <= n; i++) {
            a[i] = dm.rankOf(a[i]);
            b[i] = dm.rankOf(b[i]);
        }

        debug.debug("a", a);
        debug.debug("b", b);
        int m = dm.maxRank();
        int[] cnts = new int[m + 1];
        for (int i = 0; i <= n; i++) {
            cnts[a[i]]++;
            cnts[b[i]]--;
        }
        for (int i = 0; i <= m; i++) {
            if (cnts[i] != 0) {
                out.println(-1);
                return;
            }
        }

        Node[] nodes = new Node[m + 1];
        for (int i = 0; i <= m; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        int eCnt = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) {
                Node.merge(nodes[a[i]], nodes[b[i]]);
                //debug.debug("e", b[i] + "->" + a[i]);
                eCnt++;
            }
        }


        int cc = 0;
        for (Node node : nodes) {
            if (node != node.find()) {
                continue;
            }
            if (node.size == 1) {
                continue;
            }
            cc++;
        }

        if (cc == 0) {
            out.println(0);
            return;
        }

        if(nodes[a[n]].find().size == 1){
            cc++;
        }

        out.println(cc - 1 + eCnt);
    }

    public int xor(int[] a, int l, int r) {
        int ans = 0;
        for (int i = l; i <= r; i++) {
            ans ^= a[i];
        }
        return ans;
    }
}

class Node {
    int id;

    @Override
    public String toString() {
        return "" + id;
    }

    int size = 1;

    Node p = this;
    int rank = 0;

    Node find() {
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
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.size += b.size;
    }
}