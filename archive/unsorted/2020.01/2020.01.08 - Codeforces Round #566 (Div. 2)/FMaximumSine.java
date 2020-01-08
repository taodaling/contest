package contest;

import javafx.geometry.Pos;
import numeric.Fraction;
import template.io.FastInput;
import template.io.FastOutput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FMaximumSine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int p = in.readInt();
        int q = in.readInt();

        long ans = g(a, b, p, q);
        out.println(ans);
    }

    public long g(long a, long b, long p, long q) {
        if (p >= q) {
            return g(a, b, p % q, q);
        }
        if (p == 0) {
            return a;
        }
        Fraction t = new Fraction(q, p);
        Fraction x0 = new Fraction(1, 2).mul(t);

        //x0 + kt <= a
        //k <= (a - x0) / t
        long floor = new Fraction(a).minus(x0).div(t).floor();
        //x0 + kt >= b
        //k >= (b - x0) / t
        long ceil = new Fraction(b).minus(x0).div(t).ceil();

        Fraction[] poses = new Fraction[2];
        if (q % p == 0) {
            poses[0] = x0.plus(new Fraction(floor).mul(t));
            poses[1] = x0.plus(new Fraction(floor + 1).mul(t));
        } else {
            Fraction truncate = new Fraction(q % p, p);
            Fraction x1 = new Fraction(1, 2).mul(truncate);
            //x1 + floor * truncate <= a
            long aa = x1.plus(new Fraction(floor).mul(truncate)).ceil();
            long bb = x1.plus(new Fraction(ceil).mul(truncate)).floor();

            long intNum = g(aa, bb, p, q % p);
            Fraction position = new Fraction(intNum).minus(x1).div(truncate);
            long actualMove1 = position.floor();
            long actualMove2 = position.ceil();
            poses[0] = x0.plus(new Fraction(actualMove1).mul(t));
            poses[1] = x0.plus(new Fraction(actualMove2).mul(t));
        }

        List<Dist> dists = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            long l = poses[i].floor();
            long r = poses[i].ceil();
            l = Math.min(l, b);
            r = Math.min(r, b);
            l = Math.max(l, a);
            r = Math.max(r, a);
            dists.add(new Dist(l, poses[i].minus(new Fraction(l)).abs()));
            dists.add(new Dist(r, poses[i].minus(new Fraction(r)).abs()));
        }

        dists.sort((x, y) -> {
            int ans = x.dist.compareTo(y.dist);
            if (ans == 0) {
                ans = Long.compare(x.val, y.val);
            }
            return ans;
        });

        return dists.get(0).val;
    }
}

class Dist {
    long val;
    Fraction dist;

    public Dist(long val, Fraction dist) {
        this.val = val;
        this.dist = dist;
    }
}