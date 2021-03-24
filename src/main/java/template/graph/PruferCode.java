package template.graph;

import template.primitve.generated.utils.IntegerBinaryConsumer;

import java.util.Arrays;
import java.util.List;

//copied from https://github.com/indy256/codelibrary/blob/master/java/combinatorics/PruferCode.java
// https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence
public class PruferCode {

    // O(n) complexity
    public static void pruferCode2Tree(int[] pruferCode, IntegerBinaryConsumer consumer) {
        int n = pruferCode.length + 2;
        int[] degree = new int[n];
        Arrays.fill(degree, 1);
        for (int v : pruferCode)
            ++degree[v];
        int ptr = 0;
        while (degree[ptr] != 1)
            ++ptr;
        int leaf = ptr;
        for (int v : pruferCode) {
            consumer.accept(v, leaf);
            --degree[leaf];
            --degree[v];
            if (degree[v] == 1 && v < ptr) {
                leaf = v;
            } else {
                while (degree[++ptr] != 1) ;
                leaf = ptr;
            }
        }
        for (int v = 0; v < n - 1; v++) {
            if (degree[v] == 1) {
                consumer.accept(v, n - 1);
            }
        }
    }

    // precondition: n >= 2
    // O(n) complexity
    public static int[] tree2PruferCode(List<Integer>[] tree) {
        int n = tree.length;
        int[] parent = new int[n];
        parent[n - 1] = -1;
        pruferDfs(tree, parent, n - 1);
        int[] degree = new int[n];
        int ptr = -1;
        for (int i = 0; i < n; ++i) {
            degree[i] = tree[i].size();
            if (degree[i] == 1 && ptr == -1)
                ptr = i;
        }
        int[] res = new int[n - 2];
        for (int i = 0, leaf = ptr; i < n - 2; ++i) {
            int next = parent[leaf];
            res[i] = next;
            --degree[next];
            if (degree[next] == 1 && next < ptr) {
                leaf = next;
            } else {
                while (degree[++ptr] != 1) ;
                leaf = ptr;
            }
        }
        return res;
    }

    static void pruferDfs(List<Integer>[] tree, int[] parent, int u) {
        for (int v : tree[u]) {
            if (v != parent[u]) {
                parent[v] = u;
                pruferDfs(tree, parent, v);
            }
        }
    }
}
