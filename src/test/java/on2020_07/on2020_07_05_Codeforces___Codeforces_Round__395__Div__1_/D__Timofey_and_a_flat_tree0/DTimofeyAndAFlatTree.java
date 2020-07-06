package on2020_07.on2020_07_05_Codeforces___Codeforces_Round__395__Div__1_.D__Timofey_and_a_flat_tree0;




import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.LongHashData;
import template.rand.LongPartialHash;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class DTimofeyAndAFlatTree {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (int i = 0; i < 1; i++) {
            nonZeroCnt = 0;
            dfsForHash(nodes[0], null);
            dfsForVertex(nodes[0], null, 0);
        }


        debug.debug("vertexVal", vertexVal);
        out.println(vertex + 1);
    }

    LongHashData hd = new LongHashData((int) 1e5);
    LongPartialHash ph = new LongPartialHash(hd);
    int vertex = -1;
    int vertexVal = -1;
    LongHashMap map = new LongHashMap((int) 1e6, false);
    int nonZeroCnt = 0;


    public void update(int vertex) {
        if (nonZeroCnt > vertexVal) {
            vertexVal = nonZeroCnt;
            this.vertex = vertex;
        }
    }

    public void add(long x) {
        long val = map.get(x);
        map.put(x, val + 1);
        if (val == 0) {
            nonZeroCnt++;
        }
    }

    public void remove(long x) {
        long val = map.get(x);
        map.put(x, val - 1);
        if (val == 1) {
            nonZeroCnt--;
        }
    }

    public long hashWithout(int i, int l, int r) {
        long left = ph.hash(l, i - 1, false);
        long right = ph.hash(i + 1, r, false);
        int leftSize = i - l;
        int rightSize = r - i;
        right = hd.mod.mul(right, hd.pow[leftSize]);
        long ans = hd.mod.plus(left, right);
        ans = hd.mod.plus(ans, hd.pow[leftSize + rightSize]);
        return ans;
    }


    public void dfsForHash(Node root, Node p) {
        root.hash = new LongArrayList(root.adj.size());
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForHash(node, root);
            root.hash.add(node.hashVal);
        }
        root.hash.sort();
        ph.populate(i -> root.hash.get(i), 0, root.hash.size() - 1);
        root.hashVal = ph.hash(0, root.hash.size() - 1, true);
        add(root.hashVal);
    }

    public void dfsForVertex(Node root, Node p, long pHash) {
        if (p != null) {
            root.hash.add(pHash);
        }

        long original = root.hashVal;
        root.hash.sort();
        root.hashVal = 0;
        ph.populate(i -> root.hash.get(i), 0, root.hash.size() - 1);
        root.hashVal = ph.hash(0, root.hash.size() - 1, true);

        remove(original);
        add(root.hashVal);
        update(root.id);

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            node.mark = 0;
            int index = root.hash.binarySearch(node.hashVal);
            node.mark = hashWithout(index, 0, root.hash.size() - 1);
        }

        remove(root.hashVal);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            add(node.mark);
            dfsForVertex(node, root, node.mark);
            remove(node.mark);
        }
        add(original);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    LongArrayList hash;
    long hashVal;
    long mark;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
