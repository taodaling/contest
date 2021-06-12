package on2021_06.on2021_06_07_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.H__Alchemy;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.function.IntPredicate;

public class HAlchemy {
    public int get(int[] c, int x) {
        return x >= c.length ? 0 : c[x];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] c = in.ri(n);
        long inf = (long) 1e18;
        LongPreSum ps = new LongPreSum(i -> c[i], n);
        if (ps.post(0) == 1) {
            for (int i = 0; i < n; i++) {
                if (c[i] > 0) {
                    out.println(Math.max(1, i));
                    return;
                }
            }
        }
        IntPredicate predicate = m -> {
            if (m == 0) {
                return true;
            }
            long zero = ps.post(m) + get(c, 0);
            long req = 1;
            for (int i = m - 1; i > 0; i--) {
                long cur = get(c, i);
                if (cur >= req) {
                    zero += cur - req;
                } else {
                    req += req - cur;
                }
                if (req >= inf) {
                    return false;
                }
            }
            return zero >= req;
        };
        predicate.test(1);
        int ans = BinarySearch.lastTrue(predicate, 0, (int) 1e9);
        out.println(ans);
    }
}
