package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P1___AQT_and_Fractions;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ILongModular;
import template.math.LongPower;

import java.util.function.LongPredicate;

public class P1AQTAndFractions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        ILongModular mod = ILongModular.getInstance(b);
        LongPower pow = new LongPower(mod);
        LongPredicate predicate = mid -> {
            long v = pow.pow(10, mid);
            v = mod.mul(v, a);
            if (v == 0) {
                return true;
            }
            return false;
        };
        long first = BinarySearch.firstTrue(predicate, 0, (long) 2e18);
        if (predicate.test(first)) {
            out.println(first);
        }else{
            out.println(-1);
        }
    }
}
