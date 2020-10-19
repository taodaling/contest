package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.math.ExtGCD;
import template.math.LongExtGCDObject;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EXum {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int x = in.readInt();
        int e = Log2.floorLog(x);
        long x2 = x;
        for (int i = 0; i < e; i++) {
            x2 = add(x2, x2);
        }
        long y = xor(x, x2);
        LongExtGCDObject gcd = new LongExtGCDObject();
        gcd.extgcd(x, y);
        long a = gcd.getX();
        long b = gcd.getY();
        if (b < 0) {
            b = -b;
            if (b % 2 == 1) {
                b += x;
                a += y;
            }
        } else {
            a = -a;
            if (a % 2 == 1) {
                b += x;
                a += y;
            }
        }

        long xa = mul(x, a);
        long yb = mul(y, b);
        long ans = xor(xa, yb);
        assert ans == 1;

        out.println(op.size());
        for (String s : op) {
            out.println(s);
        }
    }

    public long mul(long x, long y) {
        if (y == 0) {
            return 0;
        }
        long x2 = mul(x, y / 2);
        x2 = add(x2, x2);
        if (y % 2 == 1) {
            x2 = add(x2, x);
        }
        return x2;
    }

    List<String> op = new ArrayList<>();

    public long add(long a, long b) {
        if (a != 0 && b != 0)
            op.add(String.format("%d + %d", a, b));
        return a + b;
    }

    public long xor(long a, long b) {
        if (a != 0 && b != 0)
            op.add(String.format("%d ^ %d", a, b));
        return a ^ b;
    }
}
