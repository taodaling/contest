package on2020_11.on2020_11_28_AtCoder___AtCoder_Regular_Contest_109.D___L;



import sun.security.provider.SHA;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;

public class DL {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] pts = new int[6];
        in.populate(pts);

        Shape target = shape(pts);
        Shape now = shape(new int[]{0, 0, 1, 0, 0, 1});

        long ans = solve(now, target);
        out.println(ans);
    }

    long inf = (long) 1e18;

    public long solve1(Shape a, Shape b) {
        long ans = 0;
        if (a.getCenterX() < b.getCenterX()) {
            if (a.x == 0) {
                ans++;
                a.x = 1;
                a.dx++;
            }
            long step2 = b.getCenterX() - a.getCenterX();
            assert step2 >= 0;
            a.dx += step2;
            ans += 2 * step2;
        }

        if (ans == 0) {
            ans += Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        } else {
            ans += Math.abs(a.x - b.x);
        }
        return ans;
    }

    public long solve2(Shape a, Shape b) {
        long ans = 0;
        long step1 = b.getCenterY() - a.getCenterY();
        assert step1 >= 0;
        ans += step1 * 2;
        a.dx += step1;
        a.dy += step1;

        long ans2 = solve1(a.clone(), b.clone());

//        if (step1 > 0) {
//            for (int i = 0; i < 2; i++) {
//                for (int j = 0; j < 2; j++) {
//                    if (i == j) {
//                        continue;
//                    }
//                    Shape ac = a.clone();
//                    Shape bc = b.clone();
//                    ac.x = i;
//                    ac.y = j;
//                    ans2 = Math.min(ans2, solve1(ac, bc));
//                }
//            }
//        }

        ans += ans2;
        return ans;
    }

    public long solve(Shape a, Shape b) {
        normalize(a, b);

        long ans = inf;
        long plus = 0;
        if (b.getCenterY() > a.getCenterY() && a.x == 0 && a.y == 0) {
            a.x++;
            a.y++;
            a.dx++;
            a.dy++;
            plus++;
        }
        if (b.getCenterY() > a.getCenterY() && a.x == 1 && a.y == 1) {
            plus++;
            {
                Shape ca = a.clone();
                Shape cb = b.clone();
                ca.x = 0;
                ans = Math.min(ans, solve2(ca, cb));
            }
            {
                Shape ca = a.clone();
                Shape cb = b.clone();
                ca.y = 0;
                ans = Math.min(ans, solve2(ca, cb));
            }
        }
        ans = Math.min(solve2(a, b), ans);
        return ans + plus;
    }

    public void normalize(Shape a, Shape b) {
        if (a.getCenterX() > b.getCenterX()) {
            a.reverseX();
            b.reverseX();
        }
        if (a.getCenterY() > b.getCenterY()) {
            a.reverseY();
            b.reverseY();
        }

        if (b.getCenterX() - a.getCenterX() < b.getCenterY() - a.getCenterY()) {
            a.transpose();
            b.transpose();
        }
    }

    public Shape shape(int[] pts) {
        long l = inf;
        long r = -inf;
        long t = -inf;
        long b = inf;
        for (int i = 0; i < 6; i += 2) {
            l = Math.min(l, pts[i]);
            r = Math.max(r, pts[i]);
        }
        for (int i = 1; i < 6; i += 2) {
            b = Math.min(b, pts[i]);
            t = Math.max(t, pts[i]);
        }

        boolean[][] occur = new boolean[2][2];

        for (int i = 0; i < 6; i += 2) {
            long x = pts[i] - l;
            long y = pts[i + 1] - b;
            occur[(int) x][(int) y] = true;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (!occur[i][j]) {
                    return new Shape(1 - i, 1 - j, l + 1 - i, b + 1 - j);
                }
            }
        }

        throw new RuntimeException();
    }
}

class Shape extends CloneSupportObject<Shape> {
    long x;
    long y;
    long dx;
    long dy;

    public void transpose() {
        long tmp = x;
        x = y;
        y = tmp;

        tmp = dx;
        dx = dy;
        dy = tmp;
    }

    public void reverseX() {
        x = 1 - x;
        dx = -dx;
    }


    public void reverseY() {
        y = 1 - y;
        dy = -dy;
    }

    public long getCenterX() {
        return dx;
    }

    public long getCenterY() {
        return dy;
    }

    public Shape(long x, long y, long dx, long dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
}