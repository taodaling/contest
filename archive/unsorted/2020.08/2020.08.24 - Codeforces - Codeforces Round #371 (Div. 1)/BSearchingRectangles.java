package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

public class BSearchingRectangles {
    FastInput in;
    FastOutput out;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        n = in.readInt();

        IntBinarySearch upDown = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(1, n, 1, mid) >= 1;
            }
        };

        IntBinarySearch leftRight = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(1, mid, 1, n) >= 1;
            }
        };
        int threshold = upDown.binarySearch(1, n);
        int[] r1;
        int[] r2;
        if (query(1, n, 1, threshold) == 1 &&
                query(1, n, threshold + 1, n) == 1) {
            r1 = find(1, n, 1, threshold);
            r2 = find(1, n, threshold + 1, n);
        } else {
            threshold = leftRight.binarySearch(1, n);
            r1 = find(1, threshold, 1, n);
            r2 = find(threshold + 1, n, 1, n);
        }

        out.append("! ");
        output(r1);
        output(r2);
        out.flush();
    }

    public void output(int[] ans) {
        for (int x : ans) {
            out.append(x).append(' ');
        }
    }

    public int[] find(int l, int r, int d, int u) {
        IntBinarySearch downIBS = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(l, r, mid, u) == 0;
            }
        };
        int y1 = downIBS.binarySearch(d, u);
        if (query(l, r, y1, u) == 0) {
            y1--;
        }

        IntBinarySearch upIBS = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(l, r, d, mid) >= 1;
            }
        };
        int y2 = upIBS.binarySearch(d, u);

        IntBinarySearch leftIBS = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(mid, r, d, u) == 0;
            }
        };
        int x1 = leftIBS.binarySearch(l, r);
        if (query(x1, r, d, u) == 0) {
            x1--;
        }

        IntBinarySearch rightIBS = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return query(l, mid, d, u) >= 1;
            }
        };
        int x2 = rightIBS.binarySearch(l, r);

        return new int[]{x1, y1, x2, y2};
    }

    public int query(int l, int r, int d, int u) {
        if (l > r || d > u) {
            return 0;
        }
        out.printf("? %d %d %d %d", l, d, r, u).println().flush();
        return in.readInt();
    }

}
