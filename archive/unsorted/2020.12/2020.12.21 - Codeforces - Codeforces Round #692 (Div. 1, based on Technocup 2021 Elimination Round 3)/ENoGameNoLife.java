package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModGussianElimination;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.Debug;

import java.util.*;

public class ENoGameNoLife {
    int mod = 998244353;
    Power pow = new Power(mod);
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
        }
        int maxSg = 0;
        for (int i = 0; i < n; i++) {
            maxSg = Math.max(maxSg, sg(nodes[i]));
        }
        int ceilLog = 1;
        while(maxSg >= ceilLog){
            ceilLog *= 2;
        }
        int[] prob = new int[ceilLog];
        int inv = pow.inverse(n + 1);
        for (int i = 0; i < n; i++) {
            prob[sg(nodes[i])] = DigitUtils.modplus(prob[sg(nodes[i])], inv, mod);
        }
        ModGussianElimination mge = new ModGussianElimination(ceilLog + 1, ceilLog + 1, mod);
        for (int i = 0; i < ceilLog + 1; i++) {
            mge.setLeft(i, i, 1);
        }
        for (int i = 0; i < ceilLog; i++) {
            for (int j = 0; j < ceilLog; j++) {
                mge.modifyLeft(i ^ j, i, -prob[j]);
            }
            mge.modifyLeft(ceilLog, i, -inv);
        }
        mge.setRight(0, 1);
        debug.debug("mge", mge);
        boolean possible = mge.solve();
        assert possible;
        int[] ans = mge.getSolutions();
        long zeroProb = (long) ans[0] * inv % mod;
        long winProb = DigitUtils.mod(1 - zeroProb, mod);
        out.println(winProb);
    }

    IntegerVersionArray iva = new IntegerVersionArray((int) 1e5);

    public int sg(Node root) {
        if (root.sg == -1) {
            for (Node node : root.adj) {
                sg(node);
            }
            iva.clear();
            for (Node node : root.adj) {
                iva.set(sg(node), 1);
            }
            root.sg = 0;
            while (iva.get(root.sg) == 1) {
                root.sg++;
            }
        }
        return root.sg;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int sg = -1;
}
