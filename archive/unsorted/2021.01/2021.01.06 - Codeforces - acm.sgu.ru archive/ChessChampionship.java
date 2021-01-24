package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

public class ChessChampionship {
    int mod = (int) 1e9 + 9;

    public int unMatched(int c0, int c1, int win, int remain) {
        int fail = c0 - win;
        int used = c1 - win - remain;
        return fail - used;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int score = in.ri();
        if ((n - score) % 2 != 0) {
            out.println(0);
            return;
        }
        int win = (n - score) / 2;
        int[] A = new int[n];
        int[] B = new int[n];
        in.populate(B);
        in.populate(A);
        Item[] items = new Item[2 * n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(0, A[i]);
        }
        for (int i = 0; i < n; i++) {
            items[i + n] = new Item(1, B[i]);
        }
        Arrays.sort(items, Comparator.comparingInt(x -> x.v));
        long[][] prev = new long[win + 1][n + 1];
        long[][] next = new long[win + 1][n + 1];
        prev[0][0] = 1;
        int[] cnts = new int[2];
        for (Item item : items) {
            debug.debug("prev", prev);
            SequenceUtils.deepFill(next, 0L);
            if (item.type == 0) {
                //A
                for (int j = 0; j <= win; j++) {
                    for (int k = 0; k <= n; k++) {
                        if (prev[j][k] == 0) {
                            continue;
                        }
                        next[j][k] += prev[j][k];
                        if (k > 0 && j + 1 <= win) {
                            next[j + 1][k - 1] += prev[j][k] * k % mod;
                        }
                    }
                }
            } else {
                //B
                for (int j = 0; j <= win; j++) {
                    for (int k = 0; k <= n; k++) {
                        if (prev[j][k] == 0) {
                            continue;
                        }
                        int t = unMatched(cnts[0], cnts[1], j, k);
                        if (k + 1 <= n) {
                            next[j][k + 1] += prev[j][k];
                        }
                        next[j][k] += t * prev[j][k] % mod;
                    }
                }
            }
            for (int j = 0; j <= win; j++) {
                for (int k = 0; k <= n; k++) {
                    next[j][k] %= mod;
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
            cnts[item.type]++;

            debug.debug("item", item);
        }
        debug.debug("prev", prev);
        long ans = prev[win][0];
        out.println(ans);
    }
}

class Item {
    int type;
    int v;

    public Item(int type, int v) {
        this.type = type;
        this.v = v;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type=" + type +
                ", v=" + v +
                '}';
    }
}
