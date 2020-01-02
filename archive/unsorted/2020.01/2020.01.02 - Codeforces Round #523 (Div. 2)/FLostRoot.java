package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.primitve.generated.IntegerList;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.CompareUtils;

import java.util.Arrays;

public class FLostRoot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        n = in.readInt();
        int k = in.readInt();

        DigitBase base = new DigitBase(k);
        int h = base.ceilLog(n * (k - 1) + 1) - 1;

        RandomWrapper wrapper = new RandomWrapper();
        int leafA = 0;
        int leafB = 0;

        for (int i = 1; i <= n; i++) {
            if (isLeaf(i)) {
                leafA = i;
                break;
            }
        }

        IntegerList path = null;
        for (int i = leafA + 1; i <= n; i++) {
            if (isLeaf(i)) {
                leafB = i;
                path = getPath(leafA, leafB);
                if (path.size() == h + h + 1) {
                    break;
                }
            }
        }
        int[] nodes = path.toArray();

        int end = leafA;
        Randomized.randomizedArray(nodes, 0, n);
        CompareUtils.quickSort(nodes, (a, b) -> query(end, a, b) ? -1 : 1, 0, nodes.length);
        int root = nodes[h];
        out.printf("! %d", root);
        out.flush();
    }

    FastInput in;
    FastOutput out;

    int n;

    public IntegerList getPath(int a, int c) {
        IntegerList ans = new IntegerList();
        for (int i = 1; i <= n; i++) {
            if (query(a, i, c)) {
                ans.add(i);
            }
        }
        return ans;
    }

    public boolean isLeaf(int x) {
        int a = x == 1 ? 2 : 1;
        for (int i = 1; i <= n; i++) {
            if (i == a || i == x) {
                continue;
            }
            if (query(a, x, i)) {
                return false;
            }
        }
        return true;
    }

    public boolean query(int a, int b, int c) {
        out.printf("? %d %d %d", a, b, c).println();
        out.flush();
        return in.readString().equals("Yes");
    }
}
