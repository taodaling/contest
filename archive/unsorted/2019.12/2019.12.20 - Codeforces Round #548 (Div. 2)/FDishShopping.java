package contest;

import template.datastructure.Array2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FDishShopping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Point[] ps = new Point[n];
        IntegerList list = new IntegerList(2 * n);

        for(int i = 0; i < n; i++){
            ps[i] = new Point();
        }
        for(int i = 0; i < n; i++){
            ps[i].x = in.readInt();
        }
        for(int i = 0; i < n; i++){
            ps[i].y = in.readInt();
        }
        for(int i = 0; i < n; i++){
            ps[i].z = in.readInt();
        }

        for (int i = 0; i < n; i++) {
            int x = ps[i].x;
            int y = ps[i].z;
            ps[i].z = ps[i].y;

            ps[i].x = x + y;
            ps[i].y = y - x;
            ps[i].val = 1;
            ps[i].id = "p" + i;
            list.add(ps[i].x);
        }

        Point[] qs = new Point[m];
        for(int i = 0; i < m; i++){
            qs[i] = new Point();
        }

        for(int i = 0; i < m; i++){
            qs[i].x = in.readInt();
        }

        for(int i = 0; i < m; i++){
            qs[i].y = in.readInt();
        }

        for (int i = 0; i < m; i++) {
            int x = qs[i].x;
            int y = qs[i].y;
            qs[i].x = x + y;
            qs[i].y = y - x;
            qs[i].z = x;
            qs[i].id = "q" + i;
            list.add(qs[i].x);
        }

        DiscreteMap dm = new DiscreteMap(list.getData());
        Point[] allPts = new Point[n + m];
        System.arraycopy(ps, 0, allPts, 0, n);
        System.arraycopy(qs, 0, allPts, n, m);
        for (Point pt : allPts) {
            pt.x = dm.rankOf(pt.x) + 1;
        }
        bit = new BIT(dm.maxRank() + 1);

        Arrays.sort(allPts, (a, b) -> {
            int ans = -Integer.compare(a.z, b.z);
            if (ans == 0) {
                ans = -Integer.compare(a.y, b.y);
            }
            if (ans == 0) {
                ans = Integer.compare(a.x, b.x);
            }
            if (ans == 0) {
                ans = -Integer.compare(a.val, b.val);
            }
            return ans;
        });

        dac(allPts, 0, n + m - 1);

        for (int i = 0; i < m; i++) {
            out.println(qs[i].ans);
        }
    }

    BIT bit;

    //z >= z0 && x <= x0 && y >= y0
    public void dac(Point[] pts, int l, int r) {
        if (l >= r) {
            pts[l].ans += pts[l].val;
            return;
        }
        int m = (l + r) >> 1;
        dac(pts, l, m);
        dac(pts, m + 1, r);

        Arrays.sort(pts, l, m + 1, (a, b) -> -Integer.compare(a.y, b.y));
        Arrays.sort(pts, m + 1, r + 1, (a, b) -> -Integer.compare(a.y, b.y));

        Array2DequeAdapter<Point> lDeque = new Array2DequeAdapter<>(pts, l, m);
        Array2DequeAdapter<Point> rDeque = new Array2DequeAdapter<>(pts, m + 1, r);
        for (int i = m + 1; i <= r; i++) {
            Point first = rDeque.removeFirst();
            while (!lDeque.isEmpty() && lDeque.peekFirst().y >= first.y) {
                Point head = lDeque.removeFirst();
                bit.update(head.x, head.val);
            }
            first.ans += bit.query(first.x);
        }
        while (!lDeque.isEmpty()) {
            Point head = lDeque.removeFirst();
            bit.update(head.x, head.val);
        }
        for (int i = l; i <= m; i++) {
            bit.update(pts[i].x, -pts[i].val);
        }
    }
}

class Point {
    int x;
    int y;
    int z;
    int val;
    int ans;

    String id;

    @Override
    public String toString() {
        return id;
    }
}