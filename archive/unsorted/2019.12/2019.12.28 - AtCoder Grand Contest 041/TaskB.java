package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        v = in.readInt();
        p = in.readInt();
        ps = new Problem[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new Problem();
            ps[i].a = in.readInt();
        }

        Arrays.sort(ps, (a, b) -> Integer.compare(a.a, b.a));
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return TaskB.this.check(mid);
            }
        };

        int index = ibs.binarySearch(0, n - 1);
        out.println(n - index);
    }

    Problem[] ps;
    int m;
    int v;
    int p;
    int n;

    public boolean check(int mid) {
        int remainChance = Math.max(0, v - (mid + 1));
        long threshold = ps[mid].a + m;
        long full = Math.max(0, remainChance - (p - 1));
        long row = 0;
        for (int i = mid + 1; i <= n - p; i++) {
            if (ps[i].a > threshold) {
                return false;
            }
            long used = Math.min(m, threshold - ps[i].a);
            row -= used;
            if (row < 0) {
                full--;
                row += m;
            }
        }
        return (full < 0 || (full == 0 && row == 0));
    }
}

class Problem {
    int a;
}
