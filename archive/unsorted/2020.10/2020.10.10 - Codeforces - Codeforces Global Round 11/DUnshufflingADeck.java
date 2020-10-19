package contest;

import template.io.FastInput;
import template.utils.CompareUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DUnshufflingADeck {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        c = new int[n];

        in.populate(c);
        int[] sorted = c.clone();
        Arrays.sort(sorted);

        int curL = n / 2;
        int curR = curL;
        int first = indexOf(0, n - 1, sorted[curL]);
        boolean head;

        apply(first, n - first);
        head = true;
        for (int i = 1; i < n; i++) {
            int l = 0, r = n - 1;
            int target;
            if (head) {
                l += i;
                target = sorted[--curL];
            } else {
                r -= i;
                target = sorted[++curR];
            }
            int j = indexOf(l, r, target);
            if (head) {
                apply(i, j - i + 1, n - i - (j - i + 1));
            } else {
                apply(j, n - j - i, i);
            }
            head = !head;
        }

        assert CompareUtils.notStrictAscending(c, 0, n - 1);
        out.println(op.size());
        for (int[] x : op) {
            out.print(x.length);
            out.append(' ');
            for (int y : x) {
                out.print(y);
                out.append(' ');
            }
            out.println();
        }
    }

    public int indexOf(int l, int r, int x) {
        for (int j = l; j <= r; j++) {
            if (c[j] == x) {
                return j;
            }
        }
        throw new RuntimeException();
    }

    int[] c;
    List<int[]> op = new ArrayList<>();

    public int[] removeZero(int[] d) {
        int wpos = 0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > 0) {
                d[wpos++] = d[i];
            }
        }
        return Arrays.copyOf(d, wpos);
    }

    public void apply(int... d) {
        d = removeZero(d);
        if (d.length <= 1) {
            return;
        }
        assert Arrays.stream(d).sum() == c.length;
        int[] next = new int[c.length];
        int wpos = 0;
        int r = c.length - 1;
        for (int i = d.length - 1; i >= 0; i--) {
            int len = d[i];
            int l = r - len + 1;
            for (int j = l; j <= r; j++) {
                next[wpos++] = c[j];
            }
            r = l - 1;
        }

        c = next;
        op.add(d);
    }
}
