package on2019_11.on2019_11_12_Codeforces_Round__569__Div__1_.C___Serge_and_Dining_Room;



import template.FastInput;
import template.FastOutput;
import template.PreSum;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int limit = (int) 1e6;
        int[] scores = new int[limit + 1];
        int[] aCnt = new int[limit + 1];
        int[] bCnt = new int[limit + 1];
        int[] a = new int[n + 1];
        int[] b = new int[m + 1];


        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
            aCnt[a[i]]++;
        }
        for (int i = 1; i <= m; i++) {
            b[i] = in.readInt();
            bCnt[b[i]]++;
        }

        scores[limit] = aCnt[limit] - bCnt[limit];
        for (int i = limit - 1; i >= 1; i--) {
            scores[i] = scores[i + 1] + aCnt[i] - bCnt[i];
        }

        Segment seg = new Segment(1, limit, scores);

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int which = in.readInt();
            int x = in.readInt();
            if (t == 1) {
                seg.update(1, a[which], 1, limit, -1);
                a[which] = x;
                seg.update(1, a[which], 1, limit, 1);
            }else{
                seg.update(1, b[which], 1, limit, 1);
                b[which] = x;
                seg.update(1, b[which], 1, limit, -1);
            }

            int ans = seg.query(1, limit);
            out.println(ans);
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int score;
    private int mod;

    public void mod(int x) {
        mod += x;
        score += x;
    }

    public void pushUp() {
        score = Math.max(left.score, right.score);
    }

    public void pushDown() {
        if (mod != 0) {
            left.mod(mod);
            right.mod(mod);
            mod = 0;
        }
    }

    public Segment(int l, int r, int[] scores) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m, scores);
            right = new Segment(m + 1, r, scores);
            pushUp();
        } else {
            score = scores[l];
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
            mod(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int l, int r) {
        Segment seg = this;
        while (l < r) {
            seg.pushDown();
            int m = (l + r) >> 1;
            if (seg.right.score > 0) {
                l = m + 1;
                seg = seg.right;
            } else {
                r = m;
                seg = seg.left;
            }
        }
        if (seg.score > 0) {
            return l;
        }
        return -1;
    }
}
