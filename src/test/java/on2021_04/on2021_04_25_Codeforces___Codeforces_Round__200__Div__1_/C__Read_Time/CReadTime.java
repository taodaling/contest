package on2021_04.on2021_04_25_Codeforces___Codeforces_Round__200__Div__1_.C__Read_Time;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.function.LongPredicate;

public class CReadTime {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] h = in.rl(n);
        long[] p = in.rl(m);
        LongPredicate predicate = mid -> {
            int head = 0;
            for (long x : h) {
                if (head >= m) {
                    break;
                }
                if (x - p[head] > mid) {
                    return false;
                }
                long r = x + mid;

                if(p[head] < x){
                    long r1 = (mid - (x - p[head]) * 2) + x;
                    long r2 = (mid - (x - p[head])) / 2 + x;
                    r = Math.max(r1, r2);
                }
//                long l1 = Math.min(p[head], x);

                while (head < m && p[head] <= r) {
                    head++;
                }
            }

            return head == m;
        };

        long ans = BinarySearch.firstTrue(predicate, 0, (long) 1e18);
        out.println(ans);
    }
}
