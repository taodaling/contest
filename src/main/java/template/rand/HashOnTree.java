package template.rand;

import template.graph.DirectedEdge;
import template.graph.UndirectedEdge;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HashOnTree {
    List<UndirectedEdge>[] g;
    long[] hash;
    long[] sub;
    IntRangeHash rh;
    int top;
    long wholeTree;
    long[] sortedHash;

    private void prepareHash() {
        if (hash == null) {
            hash = new long[sub.length];
            dfsForHash(top, -1, 0);
        }
    }

    public long hashWholeTree() {
        if (sortedHash == null) {
            prepareHash();
            sortedHash = hash.clone();
            Randomized.shuffle(sortedHash);
            Arrays.sort(sortedHash);
            rh.populate(i -> HashUtils.hash(sortedHash[i]), sortedHash.length);
            wholeTree = rh.hashV(0, sortedHash.length - 1);
        }
        return wholeTree;
    }

    public long hashOnRoot(int root) {
        if (root == top) {
            return sub[root];
        }
        prepareHash();
        return hash[root];
    }

    public HashOnTree(List<UndirectedEdge>[] g, IntRangeHash rh, int top) {
        assert g.length > 0;
        this.g = g;
        sub = new long[g.length];
        this.rh = rh;
        this.top = top;
        dfsForSub(top, -1);
    }

    private void dfsForSub(int root, int p) {
        long[] children = new long[g[root].size()];
        int len = 0;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSub(e.to, root);
            children[len++] = sub[e.to];
        }
        Randomized.shuffle(children, 0, len);
        Arrays.sort(children, 0, len);
        rh.populate(i -> HashUtils.hash(children[i]), len);
        sub[root] = rh.hashV(0, len - 1);
    }

    private void dfsForHash(int root, int p, long pHash) {
        long[] children = new long[g[root].size()];
        g[root].sort(Comparator.comparingLong(e -> e.to == p ? pHash : sub[e.to]));
        for (int i = 0; i < g[root].size(); i++) {
            DirectedEdge e = g[root].get(i);
            if (e.to == p) {
                children[i] = pHash;
            } else {
                children[i] = sub[e.to];
            }
        }
        assert SortUtils.notStrictAscending(children, 0, children.length - 1);
        rh.populate(i -> HashUtils.hash(children[i]), children.length);
        hash[root] = rh.hashV(0, children.length - 1);
        for (int i = 0; i < g[root].size(); i++) {
            children[i] = rh.hashV(0, i - 1, i + 1, children.length - 1);
        }
        for (int i = 0; i < g[root].size(); i++) {
            UndirectedEdge e = g[root].get(i);
            if (e.to == p) {
                continue;
            }
            dfsForHash(e.to, root, children[i]);
        }
    }
}
