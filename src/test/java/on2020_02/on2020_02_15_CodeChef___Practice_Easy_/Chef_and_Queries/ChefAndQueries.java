package on2020_02.on2020_02_15_CodeChef___Practice_Easy_.Chef_and_Queries;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.BitSet;

public class ChefAndQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        int s1 = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        BitSet bs = new BitSet(Integer.MAX_VALUE);
        long sum = 0;
        for (int i = 0; i < q; i++, s1 = a * s1 + b) {
            int index = (s1 >>> 1);
            if ((s1 & 1) == 1) {
                if (!bs.get(index)) {
                    bs.set(index);
                    sum += index;
                }
            } else {
                if (bs.get(index)) {
                    bs.clear(index);
                    sum -= index;
                }
            }
        }
        out.println(sum);
    }
}
