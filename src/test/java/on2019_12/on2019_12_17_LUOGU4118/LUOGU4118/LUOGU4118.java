package on2019_12.on2019_12_17_LUOGU4118.LUOGU4118;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;

import java.util.Map;

public class LUOGU4118 {
    LongPollardRho pollardRho = new LongPollardRho();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        System.err.println(testNumber);
        long n = in.readLong();
        Map<Long, Long> map = pollardRho.findAllFactors(n);
        long max = 1;
        for (long key : map.keySet()) {
            max = Math.max(max, key);
        }
        if (max == 1) {
            out.println(1);
            return;
        }
        if(max == n){
            out.println("Prime");
            return;
        }
        out.println(max);
    }
}
