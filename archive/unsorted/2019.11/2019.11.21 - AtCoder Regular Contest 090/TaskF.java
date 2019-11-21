package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.math.ExpressionSolver;
import template.math.Modular;
import template.math.Power;

public class TaskF {
    long inf = (int) 1e8;
    Modular mod = new Modular(1e9 + 7);
    DigitBase base = new DigitBase(10);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int s = in.readInt();
        ExpressionSolver expressionSolver = new ExpressionSolver();
        int ans = 0;

        // differ for zero
        IntList list = new IntList();
        for (int i = 1; i * i <= s; i++) {
            if (s % i != 0) {
                continue;
            }
            list.add(i);
            list.add(s / i);
        }
        list.unique();
        for (int i = 0; i < list.size(); i++) {
            int factor = list.get(i);
            int req = s / factor;
            int count = mod.subtract(power.pow(10, factor), power.pow(10, factor - 1));
            if (factor <= 9 && count < req) {
                continue;
            }
            count = mod.subtract(count, req - 1);
            ans = mod.plus(ans, count);
        }

        // differ for one
        for (int i = 1; i + i + 1 <= s; i++) {
            long atLeast = s % i;
            if (i + atLeast * (i + 1) > s) {
                continue;
            }
            long xr = inf;
            long yr = inf;
            if (i <= 10) {
                xr = base.valueOfBit(i) - base.valueOfBit(i - 1);
                yr = base.valueOfBit(i + 1) - base.valueOfBit(i);
            }

            long way = expressionSolver.findWaysToAssignXYSatisfyEquation(i, i + 1, s, 1, xr, 1, yr);
            ans = mod.plus(ans, way);
        }

        // differ for more
        for (int i = 1; i <= 9; i++) {
            for (int j = i + 2; j <= 9; j++) {
                long remain = s;
                for (int k = i + 1; k < j; k++) {
                    remain -= k * (base.valueOfBit(k) - base.valueOfBit(k - 1));
                }
                if (remain < 0) {
                    continue;
                }
                long way = expressionSolver.findWaysToAssignXYSatisfyEquation(i, j, remain, 1,
                                base.valueOfBit(i) - base.valueOfBit(i - 1), 1,
                                base.valueOfBit(j) - base.valueOfBit(j - 1));
                ans = mod.plus(ans, way);
            }
        }

        out.println(ans);
    }
}
