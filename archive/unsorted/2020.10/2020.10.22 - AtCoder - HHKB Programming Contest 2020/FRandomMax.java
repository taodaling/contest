package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.math.Power;
import template.polynomial.IntPoly;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FRandomMax {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        List<Interval> intervals = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            intervals.add(new Interval(in.readInt(), in.readInt()));
        }

        int maxL = intervals.stream().mapToInt(x -> x.l).max().orElse(-1);
        IntegerArrayList list = new IntegerArrayList();
        for (Interval x : intervals) {
            list.add(x.l);
            list.add(x.r);
        }
        list.unique();
        List<Interval> fragments = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            fragments.add(new Interval(list.get(i - 1), list.get(i)));
        }
        boolean[] added = new boolean[n];
        SequenceUtils.reverse(fragments);
        int mod = (int) (1e9 + 7);
        Power pow = new Power(mod);
        long exp = 0;
        int[] poly = PrimitiveBuffers.allocIntPow2(1);
        IntPoly intPoly = new IntPoly(mod);
        poly[0] = 1;
        for (Interval fragment : fragments) {
            if (fragment.l < maxL) {
                break;
            }
            for (int i = 0; i < n; i++) {
                Interval interval = intervals.get(i);
                if (added[i] || interval.r < fragment.r) {
                    continue;
                }
                added[i] = true;
                int[] single = PrimitiveBuffers.allocIntPow2(2);
                int inv = pow.inverse(interval.r - interval.l);
                single[0] = DigitUtils.mod((long) -interval.l * inv, mod);
                single[1] = inv;
                poly = PrimitiveBuffers.replace(intPoly.convolution(poly, single), poly, single);
            }
            int[] delta = intPoly.differential(poly);
            int[] moved = PrimitiveBuffers.replace(intPoly.rightShift(delta, 1), delta);
            int[] integration = PrimitiveBuffers.replace(intPoly.integral(moved), moved);
            exp += intPoly.apply(integration, fragment.r) - intPoly.apply(integration, fragment.l);
        }

        exp = DigitUtils.mod(exp, mod);
        for (int i = 1; i <= n + 1; i++) {
            exp = exp * i % mod;
        }
        for (Interval interval : intervals) {
            exp = exp * (interval.r - interval.l) % mod;
        }
        out.println(exp);
    }
}

class Interval {
    int l;
    int r;

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }
}
