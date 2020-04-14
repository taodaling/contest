package on2020_04.on2020_04_14_Codeforces_Round__488_by_NEAR__Div__1_.A__Two_Squares;



import template.io.FastInput;
import template.io.FastOutput;

public class ATwoSquares {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Rect r1 = new Rect();
        Rect r2 = new Rect();
        for (int i = 0; i < 4; i++) {
            r1.update(in.readInt(), in.readInt());
        }
        for (int i = 0; i < 4; i++) {
            r2.update(in.readInt(), in.readInt());
        }

        for (int x = -100; x <= 100; x++) {
            for (int y = -100; y <= 100; y++) {
                if (contain1(r1, x, y) && contain2(r2, x, y)) {
                    out.println("YES");
                    return;
                }
            }
        }
        out.println("NO");
    }

    public boolean contain1(Rect rect, int x, int y) {
        return x >= rect.l && x <= rect.r && y >= rect.b && y <= rect.t;
    }

    public boolean contain2(Rect rect, int x, int y) {
        int cx = (rect.l + rect.r) / 2;
        int cy = (rect.b + rect.t) / 2;
        int dist = Math.abs(cx - x) + Math.abs(cy - y);
        return dist <= cx - rect.l;
    }
}

class Rect {
    int l = 100;
    int r = -100;
    int t = -100;
    int b = 100;

    public void update(int x, int y) {
        l = Math.min(l, x);
        r = Math.max(r, x);
        t = Math.max(t, y);
        b = Math.min(b, y);
    }
}
