package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ANEKOsMazeGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        Segment seg = new Segment(2, n);

        int[][] mat = new int[2][n + 1];
        for (int i = 0; i < q; i++) {
            int r = in.readInt() - 1;
            int c = in.readInt();
            int old = mat[r][c];
            mat[r][c] = 1 - mat[r][c];
            int diff = mat[r][c] - old;
            if (r == 0) {
                seg.update(c, c, 2, n, diff, 0);
                seg.update(c + 1, c + 1, 2, n, diff, 0);
            } else {
                seg.update(c, c, 2, n, 0, diff);
                seg.update(c + 1, c + 1, 2, n, 0, diff);
            }

            if(seg.max == 2){
                out.println("No");
            }else{
                out.println("Yes");
            }
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int max;
    private int top;
    private int bot;

    public void modifyTop(int x) {
        top += x;
        max = Integer.signum(top) + Integer.signum(bot);
    }

    public void modifyBot(int x) {
        bot += x;
        max = Integer.signum(top) + Integer.signum(bot);
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
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

    public void update(int ll, int rr, int l, int r, int t, int b) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modifyBot(b);
            modifyTop(t);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, t, b);
        right.update(ll, rr, m + 1, r, t, b);
        pushUp();
    }
}
