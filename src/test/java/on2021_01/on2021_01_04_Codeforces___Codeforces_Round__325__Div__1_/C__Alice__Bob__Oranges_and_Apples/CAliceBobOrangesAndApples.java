package on2021_01.on2021_01_04_Codeforces___Codeforces_Round__325__Div__1_.C__Alice__Bob__Oranges_and_Apples;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.utils.Debug;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CAliceBobOrangesAndApples {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.rl();
        long y = in.rl();
        if (GCDs.gcd(x, y) != 1) {
            out.println("Impossible");
            return;
        }

        dfs(BigInteger.valueOf(0), BigInteger.valueOf(1),
                BigInteger.valueOf(1), BigInteger.valueOf(0),
                BigInteger.valueOf(x), BigInteger.valueOf(y));

        debug.debug("op", op);
        long a = 1;
        long b = 0;
        long c = 0;
        long d = 1;
        for (Op t : op) {
            out.append(t.step);
            out.append(t.dir == 'L' ? 'B' : 'A');
            if (t.dir == 'R') {
                c += a * t.step;
                d += b * t.step;
            }else{
                a += c * t.step;
                b += d * t.step;
            }
        }
        assert a + c == x && b + d == y;
    }

    List<Op> op = new ArrayList<>();

    public int compareFraction(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
        return a.multiply(d)
                .compareTo(b.multiply(c));
    }

    public void dfs(BigInteger a, BigInteger b, BigInteger c, BigInteger d, BigInteger x, BigInteger y) {
        int comp = compareFraction(a.add(c), b.add(d), x, y);
        if (comp == 0) {
            return;
        }

        if (comp > 0) {
            BigInteger[] div = x.multiply(d).subtract(c.multiply(y))
                    .divideAndRemainder(a.multiply(y).subtract(b.multiply(x)));
            if (div[1].signum() == 0) {
                div[0] = div[0].subtract(BigInteger.ONE);
            }
            BigInteger k = div[0];
            op.add(new Op('L', k.longValue()));
            dfs(a, b, k.multiply(a).add(c), k.multiply(b).add(d), x, y);
        } else {
            BigInteger[] div = x.multiply(b).subtract(a.multiply(y))
                    .divideAndRemainder(c.multiply(y).subtract(d.multiply(x)));
            if (div[1].signum() == 0) {
                div[0] = div[0].subtract(BigInteger.ONE);
            }
            BigInteger k = div[0];
            op.add(new Op('R', k.longValue()));
            dfs(k.multiply(c).add(a), k.multiply(d).add(b), c, d, x, y);
        }
    }
}

class Op {
    char dir;
    long step;

    public Op(char dir, long step) {
        this.dir = dir;
        this.step = step;
    }

    @Override
    public String toString() {
        return "" + step + "" + dir;
    }
}