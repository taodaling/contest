package contest;

import template.FastInput;
import template.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] lrs = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                lrs[i][j] = in.readInt();
            }
        }

        Segment seg = new Segment(0, m);
        Arrays.sort(lrs, (a, b) -> len(a) - len(b));
        Deque<int[]> deque = new ArrayDeque<>(Arrays.asList(lrs));
        for (int i = 1; i <= m; i++) {
            while (!deque.isEmpty() && len(deque.peekFirst()) < i) {
                int[] lr = deque.removeFirst();
                seg.update(lr[0], lr[1], 0, m, 1);
            }
            int ans = deque.size();
            for(int j = 0; j <= m; j += i){
                ans += seg.queryMax(j, j, 0, m);
            }

            out.println(ans);
        }
    }

    public int len(int[] lr) {
        return lr[1] - lr[0] + 1;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int cnt;

    public void plus(int x) {
        cnt += x;
    }

    public void pushUp() {
    }

    public void pushDown() {
        if(cnt != 0){
            left.plus(cnt);
            right.plus(cnt);
            cnt = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            plus(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return cnt;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.queryMax(ll, rr, l, m) +
        right.queryMax(ll, rr, m + 1, r);
    }
}