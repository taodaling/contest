package contest;

import template.primitve.generated.datastructure.IntegerBinaryFunction;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BoxTower {
    public int tallestTower(int[] x, int[] y, int[] z) {
        int n = x.length;
        boxes = new Box[n];
        IntegerList list = new IntegerList();
        list.addAll(x);
        list.addAll(y);
        list.addAll(z);
        list.add(0);
        list.unique();
        dm = list.toArray();
        for (int i = 0; i < n; i++) {
            x[i] = list.binarySearch(x[i]);
            y[i] = list.binarySearch(y[i]);
            z[i] = list.binarySearch(z[i]);
        }

        for (int i = 0; i < n; i++) {
            boxes[i] = new Box();
            boxes[i].xyz[0] = x[i];
            boxes[i].xyz[1] = y[i];
            boxes[i].xyz[2] = z[i];
            Randomized.shuffle(boxes[i].xyz);
        }
        sorted = boxes.clone();

        dfs(n - 1, 0);
        return ans;
    }

    IntegerGenericBIT bit = new IntegerGenericBIT(50, Math::max, 0);
    int[] dm;
    Box[] boxes;
    Box[] sorted;
    int ans = 0;

    public void solve() {
        int n = boxes.length;
        for (int i = 0; i < n; i++) {
            if (sorted[i].xyz[0] < sorted[i].xyz[1]) {
                sorted[i].min = sorted[i].xyz[0];
                sorted[i].max = sorted[i].xyz[1];
            } else {
                sorted[i].min = sorted[i].xyz[1];
                sorted[i].max = sorted[i].xyz[0];
            }
        }

        Arrays.sort(sorted, (a, b) -> a.max == b.max ? a.min - b.min : a.max - b.max);
        for (Box b : sorted) {
            int h = bit.query(b.min) + dm[(b.xyz[2])];
            bit.update(b.min, h);
        }
        ans = Math.max(ans, bit.query(50));
        bit.clear();
    }

    public void dfs(int n, int sum) {
        if (n == -1) {
            if (sum > ans) {
                solve();
            }
            return;
        }
        for (int i = 2; i >= 0; i--) {
            SequenceUtils.swap(boxes[n].xyz, i, 2);
            dfs(n - 1, sum + dm[(boxes[n].xyz[2])]);
        }
    }

}

class Box {
    int[] xyz = new int[3];
    int max;
    int min;

}

class IntegerGenericBIT {
    private IntegerVersionArray data;
    private int n;
    private IntegerBinaryFunction merger;
    private int unit;

    /**
     * 创建大小A[1...n]
     */
    public IntegerGenericBIT(int n, IntegerBinaryFunction merger, int unit) {
        this.n = n;
        data = new IntegerVersionArray(n + 1, unit);
        this.merger = merger;
        this.unit = unit;
        clear();
    }

    /**
     * 查询A[1]+A[2]+...+A[i]
     */
    public int query(int i) {
        int sum = unit;
        for (; i > 0; i -= i & -i) {
            sum = merger.apply(sum, data.get(i));
        }
        return sum;
    }

    /**
     * 将A[i]更新为A[i]+mod
     */
    public void update(int i, int mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data.set(i, merger.apply(data.get(i), mod));
        }
    }

    /**
     * 将A全部清0
     */
    public void clear() {
        data.clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
