package on2021_01.on2021_01_23_Luogu.P1748_H_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class P1748H {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        PriorityQueue<BigInteger> pq = new PriorityQueue<>(Math.max(n, 1) * 4);
        Set<BigInteger> handled = new HashSet<>(n);
        pq.add(BigInteger.ONE);
        BigInteger last = BigInteger.ZERO;
        int[] mul = new int[]{2, 3, 5, 7};
        while (n > 0) {
            BigInteger head = pq.remove();
            if (!handled.add(head)) {
                continue;
            }
            n--;
            last = head;
            for (int x : mul) {
                pq.add(last.multiply(BigInteger.valueOf(x)));
            }
        }
        out.println(last);
    }
}
