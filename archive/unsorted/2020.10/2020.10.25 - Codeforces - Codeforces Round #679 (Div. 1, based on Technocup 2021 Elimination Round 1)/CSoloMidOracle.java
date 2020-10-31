package contest;

import template.algo.LongTernarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.function.LongUnaryOperator;

public class CSoloMidOracle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        long c = in.readInt();
        long d = in.readInt();
        if (a > c * b) {
            out.println(-1);
            return;
        }
        LongUnaryOperator lop = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long x) {
                long r = b * d;
                return a * (x + 1) - b * d * ((1 + x) * x / 2);
            }
        };

        LongTernarySearch lts = new LongTernarySearch(lop);
        long time = lts.find(0, Math.min(c / d, 2 * a / b / d));
        long damage = lop.applyAsLong(time);
        out.println(damage);
    }
}
