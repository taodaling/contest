package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.math.CachedLog2;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.MultiWayIntegerStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GSourcesAndSinks {
    static MultiWayIntegerStack edges;
    static boolean[] isSink;
    static boolean[] isSrc;
    static int[] id;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        edges = new MultiWayIntegerStack(n, m);
        isSink = new boolean[n];
        isSrc = new boolean[n];
        Arrays.fill(isSink, true);
        Arrays.fill(isSrc, true);

        id = new int[n];
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges.addLast(a, b);
            isSink[a] = false;
            isSrc[b] = false;
        }

        int k = 0;
        for (int node = 0; node < n; node++) {
            if (isSink[node]) {
                id[node] = k++;
            }
        }

        IntegerList masks = new IntegerList(k);
        for (int node = 0; node < n; node++) {
            if (isSrc[node]) {
                masks.add(dfs(node));
            }
        }

        int[] access = masks.toArray();
        int[] merged = new int[1 << k];
        for (int i = 1; i < merged.length; i++) {
            int lb = Integer.lowestOneBit(i);
            merged[i] = merged[i - lb] | access[CachedLog2.floorLog(lb)];
        }

        for (int i = 1; i < merged.length - 1; i++) {
            if (Integer.bitCount(i) >= Integer.bitCount(merged[i])) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }

    public static int dfs(int root) {
        int mask = 0;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            mask |= dfs(iterator.next());
        }
        if (isSink[root]) {
            mask = Bits.setBit(mask, id[root], true);
        }
        return mask;
    }
}

