package on2020_04.on2020_04_20_Codeforces___Codeforces_Round__470__rated__Div__1__based_on_VK_Cup_2018_Round_1_.B__Producing_Snow;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.PriorityQueue;

public class BProducingSnow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] V = new long[n];
        for (int i = 0; i < n; i++) {
            V[i] = in.readInt();
        }
        long[] T = new long[n];
        for (int i = 0; i < n; i++) {
            T[i] = in.readInt();
        }
        LongPreSum lps = new LongPreSum(T);
        for (int i = 0; i < n; i++) {
            V[i] += lps.prefix(i - 1);
        }

        PriorityQueue<Long> pq = new PriorityQueue<>(n);
        for (int i = 0; i < n; i++) {
            pq.add(V[i]);
            long today = 0;
            while (!pq.isEmpty() && pq.peek() <= lps.prefix(i)) {
                today += pq.remove() - lps.prefix(i - 1);
            }
            today += pq.size() * T[i];
            out.append(today).append(' ');
        }
    }
}
