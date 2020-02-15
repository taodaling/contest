package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.polynomial.GravityLagrangeInterpolation;
import template.polynomial.GravityModLagrangeInterpolation;
import template.primitve.generated.IntegerList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FGoodContest {
    Modular mod = new Modular(998244353);
    Power power = new Power(mod);
    GravityModLagrangeInterpolation zero = new GravityModLagrangeInterpolation(mod, 1);
    GravityModLagrangeInterpolation one = new GravityModLagrangeInterpolation(mod, 1);

    {
        zero.addPoint(0, 0);
        one.addPoint(0, 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] lr = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                lr[i][j] = in.readInt();
            }
        }

        SequenceUtils.reverse(lr, 0, n - 1);
        SegmentFunctions functions = new SegmentFunctions();
        functions.add(new SegmentFunction(-1, lr[0][0] - 1, zero));
        functions.add(new SegmentFunction(lr[0][0], lr[0][1], one));
        functions.add(new SegmentFunction(lr[0][1] + 1, (int) 1e9, zero));

        for (int i = 1; i < n; i++) {
            functions = build(functions, lr[i][0], lr[i][1], true, i + 1);
        }

        functions = build(functions, 0, (int) 1e9 - 1, false, n + 1);
        int valid = functions.apply((int) 1e9).p.getYByInterpolation((int) 1e9);
        int total = 1;
        for (int[] x : lr) {
            total = mod.mul(total, x[1] - x[0] + 1);
        }
        int ans = mod.mul(valid, power.inverseByFermat(total));
        out.println(ans);
    }

    public SegmentFunctions build(SegmentFunctions functions, int l, int r, boolean addTail, int trainLimit) {
        List<Interval> intervals = new ArrayList<>();
        for (SegmentFunction x : functions.functionTreeSet.values()) {
            intervals.add(x.interval);
        }
        for (Interval interval : intervals) {
            if (interval.contain(l)) {
                intervals.remove(interval);
                intervals.addAll(Arrays.asList(interval.split(l - 1)));
                break;
            }
        }
        for (Interval interval : intervals) {
            if (interval.contain(r)) {
                intervals.remove(interval);
                intervals.addAll(Arrays.asList(interval.split(r)));
                break;
            }
        }
        intervals = intervals.stream().filter(x -> !x.empty()).collect(Collectors.toList());
        intervals.sort((a, b) -> a.l - b.l);
        SegmentFunctions ans = new SegmentFunctions();
        ans.add(new SegmentFunction(intervals.get(0).l - 1, intervals.get(0).l - 1, zero));
        for (Interval interval : intervals) {
            if (interval.l > r) {
                break;
            }
            GravityModLagrangeInterpolation interpolation = new GravityModLagrangeInterpolation(mod, trainLimit);
            SegmentFunction tail = ans.functionTreeSet.lastEntry().getValue();
            SegmentFunction bot = functions.apply(interval.l);
            int ps = tail.p.getYByInterpolation(interval.l - 1);
            for (int i = 0; i < trainLimit; i++) {
                int x = mod.valueOf(interval.l + i);
                ps = mod.plus(ps, bot.p.getYByInterpolation(x));
                interpolation.addPoint(x, ps);
            }
            ans.add(new SegmentFunction(interval.l, interval.r, interpolation));
        }
        while (ans.functionTreeSet.firstKey() < l) {
            ans.functionTreeSet.pollFirstEntry();
        }
        ans.add(new SegmentFunction(intervals.get(0).l, l - 1, zero));
        if(addTail) {
            ans.add(new SegmentFunction(r + 1, intervals.get(intervals.size() - 1).r, zero));
        }
        return ans;
    }
}

class SegmentFunctions {
    TreeMap<Integer, SegmentFunction> functionTreeSet = new TreeMap<>();

    public void add(SegmentFunction function) {
        functionTreeSet.put(function.interval.l, function);
    }

    public SegmentFunction apply(int x) {
        SegmentFunction function = functionTreeSet.floorEntry(x).getValue();
        return function;
    }
}

class Interval {
    int l;
    int r;

    public boolean contain(int x) {
        return l <= x && x <= r;
    }

    public boolean empty() {
        return l > r;
    }

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }

    public Interval[] split(int m) {
        return new Interval[]{new Interval(l, m), new Interval(m + 1, r)};
    }

    @Override
    public String toString() {
        return "(" + l + "," + r + ")";
    }
}

class SegmentFunction {
    Interval interval;
    GravityModLagrangeInterpolation p;

    public SegmentFunction(int l, int r, GravityModLagrangeInterpolation p) {
        this.interval = new Interval(l, r);
        this.p = p;
    }
}
