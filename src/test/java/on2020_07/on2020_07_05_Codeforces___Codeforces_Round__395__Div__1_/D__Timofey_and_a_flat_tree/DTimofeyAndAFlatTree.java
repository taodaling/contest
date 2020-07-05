package on2020_07.on2020_07_05_Codeforces___Codeforces_Round__395__Div__1_.D__Timofey_and_a_flat_tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.HashData;
import template.rand.Hasher;
import template.rand.PartialHash;
import template.rand.RandomWrapper;
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
            hds = new HashData[]{new HashData((int) 1e5, (int) 1e9 + 7, RandomWrapper.INSTANCE.nextInt(3, (int) (1e9 + 6))), new HashData((int) 1e5, (int) 1e9 + 7, RandomWrapper.INSTANCE.nextInt(3, (int) (1e9 + 6)))};
            phs = new PartialHash[]{new PartialHash(hds[0]), new PartialHash(hds[1])};
            nonZeroCnt = 0;
            dfsForHash(nodes[0], null);
            dfsForVertex(nodes[0], null, 0);
        }


        debug.debug("vertexVal", vertexVal);
        out.println(vertex + 1);
    }

    HashData[] hds;//= new HashData[]{new HashData((int) 1e5, (int) 1e9 + 7, RandomWrapper.INSTANCE.nextInt(3, (int) (1e9 + 6))), new HashData((int) 1e5, (int) 1e9 + 7, RandomWrapper.INSTANCE.nextInt(3, (int) (1e9 + 6)))};
    PartialHash[] phs;//= new PartialHash[]{new PartialHash(hds[0]), new PartialHash(hds[1])};
    long mask = (1L << 32) - 1;
    int vertex = -1;
    int vertexVal = -1;
    LongHashMap map = new LongHashMap((int) 2e6, true);
    int nonZeroCnt = 0;
    Hasher hasher = new Hasher();

    Modular mod = new Modular(1e9 + 7);

    public int hashLong(long x) {
        return hasher.hash(x);
    }

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

    public int hashWithout(int h, int i, int l, int r) {
        int left = phs[h].hash(l, i - 1, false);
        int right = phs[h].hash(i + 1, r, false);
        int leftSize = i - l;
        int rightSize = r - i;
        right = hds[h].mod.mul(right, hds[h].pow[leftSize]);
        int ans = hds[h].mod.plus(left, right);
        ans = hds[h].mod.plus(ans, hds[h].pow[leftSize + rightSize]);
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
        phs[0].populate(i -> hashLong(root.hash.get(i)), 0, root.hash.size() - 1);
        phs[1].populate(i -> hashLong(root.hash.get(i)), 0, root.hash.size() - 1);
        for (PartialHash ph : phs) {
            root.hashVal = (root.hashVal << 32) | (ph.hash(0, root.hash.size() - 1, true) & mask);
        }
        add(root.hashVal);
    }

    public void dfsForVertex(Node root, Node p, long pHash) {
        if (p != null) {
            root.hash.add(pHash);
        }

        long original = root.hashVal;
        root.hash.sort();
        root.hashVal = 0;
        phs[0].populate(i -> hashLong(root.hash.get(i)), 0, root.hash.size() - 1);
        phs[1].populate(i -> hashLong(root.hash.get(i)), 0, root.hash.size() - 1);
        for (PartialHash ph : phs) {
            root.hashVal = (root.hashVal << 32) | (ph.hash(0, root.hash.size() - 1, true) & mask);
        }

        remove(original);
        add(root.hashVal);
        update(root.id);

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            node.mark = 0;
            int index = root.hash.binarySearch(node.hashVal);
            for (int i = 0; i < 2; i++) {
                node.mark = (node.mark << 32) | (hashWithout(i, index, 0, root.hash.size() - 1) & mask);
            }
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
