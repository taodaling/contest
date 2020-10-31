package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Sum_of_Four_Values;



import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class SumOfFourValues {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long x = in.readInt();
        int[] p = new int[n];
        in.populate(p);
        LongHashMap hm = new LongHashMap(n * n, false);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long sum = p[i] + p[j];
                long req = x - sum;
                long ans = hm.getOrDefault(req, -1);
                if (ans != -1) {
                    out.println(DigitUtils.highBit(ans) + 1);
                    out.println(DigitUtils.lowBit(ans) + 1);
                    out.println(i + 1);
                    out.println(j + 1);
                    return;
                }
            }
            for (int j = 0; j < i; j++) {
                hm.put(p[i] + p[j], DigitUtils.asLong(i, j));
            }
        }
        out.println("IMPOSSIBLE");
    }
}
