package on2020_09.on2020_09_19_AtCoder___AtCoder_Grand_Contest_047.D___Twin_Binary_Trees;



import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeInverseNumber;
import template.math.Modular;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class DTwinBinaryTrees {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();

        Node[] tree1 = create(h);
        Node[] tree2 = create(h);
        int[] perm = new int[1 << h - 1];
        in.populate(perm);

        bit = new ModBIT(1 << h, mod.getMod());
        for (int i = 0; i < perm.length; i++) {
            Node a = tree1[i + (1 << h - 1)];
            int delta = perm[i] - (i + 1);
            Node b = tree2[a.id + delta];
            a.perm = b;
            b.perm = a;
        }


        dfsForRecord(tree1[1]);
        dfs1(tree1[1]);
        sum = mod.valueOf(sum);
        sum = mod.mul(sum, inverseNumber.inverse(2));
        out.println(sum);
    }

    long sum = 0;
    ModBIT bit;

    public void mul(int i, int x) {
        int old = bit.query(i, i);
        int next = mod.mul(old, x);
        bit.update(i, next - old);
    }

    public void dfsForRecord(Node root) {
        if (root.adj.isEmpty()) {
            bit.update(root.perm.id, 1);
            mul(root.perm.id, root.prod);
            mul(root.perm.id, root.perm.prod);
        }
        for (Node node : root.adj) {
            dfsForRecord(node);
        }
    }

    public void intervalMul(Node root, int mul) {
        if (root.adj.isEmpty()) {
            long cur = (long) root.prod * root.perm.prod % modVal
                    * mul % modVal;

            mul(root.perm.id, mul);
            return;
        }
        for (Node node : root.adj) {
            intervalMul(node, mul);
        }
    }

    public int pow2(int x) {
        return mod.mul(x, x);
    }

    Debug debug = new Debug(false);

    public void dfs1(Node root) {
        if (root.p != null) {
            intervalMul(root, mod.mul(inverseNumber.inverse(root.id),
                    inverseNumber.inverse(root.p.id)));
        }

        if (root.adj.isEmpty()) {
            dfs2(root.perm.p, root.perm, mod.mul(root.prod, root.perm.prod));
        }

        //remove parent
        for (Node node : root.adj) {
            dfs1(node);
        }

        if (root.p != null) {

            intervalMul(root, mod.mul(root.id,
                    root.p.id));
        }
    }

    public void dfs2(Node root, Node child, int mul) {
        if (root == null) {
            return;
        }
        for (Node node : root.adj) {
            if (node == child) {
                continue;
            }
            int val = bit.query(node.l, node.r);
            val = mod.mul(val, mul);
            val = mod.mul(val, pow2(root.invProd));
            val = mod.mul(val, root.id);
            sum += val;
            debug.debug("val", val);
        }
        dfs2(root.p, root, mul);
    }

    public Node[] create(int h) {
        Node[] nodes = new Node[1 << h];
        for (int i = 1; i < 1 << h; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < 1 << h; i++) {
            int l = i * 2;
            int r = l + 1;
            if (l >= 1 << h) {
                continue;
            }

            nodes[i].adj.add(nodes[l]);
            nodes[i].adj.add(nodes[r]);
        }

        dfs(nodes[1], null, 1, 1);
        return nodes;
    }

    public void dfs(Node root, Node p, int ps, int inv) {
        root.p = p;
        root.prod = mod.mul(ps, root.id);
        root.invProd = mod.mul(inv, inverseNumber.inverse(root.id));

        if (root.adj.isEmpty()) {
            root.r = root.l = root.id;
        } else {
            root.l = (int) 1e9;
            root.r = -1;
            for (Node node : root.adj) {
                dfs(node, root, root.prod, root.invProd);
                root.l = Math.min(root.l, node.l);
                root.r = Math.max(root.r, node.r);
            }
        }
    }

    Modular mod = new Modular(1e9 + 7);
    int modVal = mod.getMod();
    ModPrimeInverseNumber inverseNumber = new ModPrimeInverseNumber((int) 3e5, mod);
}

class Node {
    int id;
    int prod;
    int invProd;
    List<Node> adj = new ArrayList<>();

    int l;
    int r;

    Node p;
    Node perm;

    @Override
    public String toString() {
        return "" + id;
    }
}