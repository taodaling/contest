package on2021_04.on2021_04_13_Codeforces___Codeforces_Round__207__Div__1_.D__Bags_and_Coins;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class DBagsAndCoins {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int s = in.ri();
        int[] a = in.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> -Integer.compare(a[i], a[j]), 0, n);
        BitSet next = new BitSet(s + 1);
        BitSet prev = new BitSet(s + 1);
        int[] choice = new int[s + 1];
        Arrays.fill(choice, -1);

        if (a[indices[0]] > s) {
            out.println(-1);
            return;
        }
        prev.set(a[indices[0]]);
        choice[a[indices[0]]] = indices[0];
        for (int i = 1; i < n; i++) {
            int index = indices[i];
            next.copy(prev);
            next.rightShift(a[index]);
            next.or(prev);
            prev.xor(next);

            for (int j = prev.nextSetBit(0); j < prev.capacity(); j = prev.nextSetBit(j + 1)) {
                choice[j] = index;
            }

            BitSet tmp = prev;
            prev = next;
            next = tmp;
        }

        if (!prev.get(s)) {
            out.println(-1);
            return;
        }

        boolean[] root = new boolean[n];
        for (int cur = s; cur > 0; cur -= a[choice[cur]]) {
            root[choice[cur]] = true;
        }
        debug.debugArray("root", root);
        int[] go = new int[n];
        Arrays.fill(go, -1);
        int last = indices[0];
        for (int i = 1; i < n; i++) {
            int index = indices[i];
            if (root[index]) {
                continue;
            }
            go[last] = index;
            last = index;
        }

        for (int i = 0; i < n; i++) {
            int size = a[i];
            if (go[i] != -1) {
                size -= a[go[i]];
            }
            out.append(size).append(' ');
            if (go[i] == -1) {
                out.append(0);
            } else {
                out.append(1).append(' ').append(go[i] + 1);
            }
            out.println();
        }
    }
}
