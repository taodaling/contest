package on2021_10.on2021_10_30_AtCoder___AtCoder_Beginner_Contest_224.G___Roll_or_Increment;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;

import java.util.function.IntToDoubleFunction;

public class GRollOrIncrement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int s = in.ri();
        int t = in.ri();
        int b = in.ri();
        int a = in.ri();

        IntToDoubleFunction f = m -> {
            double ans = (double) b / (t - m + 1) * IntMath.sumOfInterval(0, t - m) + (double) a * n / (t - m + 1);
            return -ans;
        };
        IntTernarySearch ts = new IntTernarySearch(f);
        int ans = ts.find(1, t);
        double cost = -f.applyAsDouble(ans);
        if (s <= t) {
            cost = Math.min(cost, (double) (t - s) * b);
        }
        out.println(cost);
    }
}

class IntTernarySearch {
    private IntToDoubleFunction operator;

    public IntTernarySearch(IntToDoubleFunction operator) {
        this.operator = operator;
    }

    public int find(int l, int r) {
        while (r - l > 2) {
            int ml = l + DigitUtils.floorDiv(r - l, 3);
            int mr = r - DigitUtils.ceilDiv(r - l, 3);
            if (operator.applyAsDouble(ml) < operator.applyAsDouble(mr)) {
                l = ml;
            } else {
                r = mr;
            }
        }
        while (l < r) {
            if (operator.applyAsDouble(l) >= operator.applyAsDouble(r)) {
                r--;
            } else {
                l++;
            }
        }
        return l;
    }
}