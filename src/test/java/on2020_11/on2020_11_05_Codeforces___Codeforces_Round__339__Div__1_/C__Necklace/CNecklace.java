package on2020_11.on2020_11_05_Codeforces___Codeforces_Round__339__Div__1_.C__Necklace;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;

import java.util.Deque;

public class CNecklace {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int g = 0;
        for (int x : a) {
            g = GCDs.gcd(g, x);
        }
        int ans = 0;
        boolean odd = true;
        for (int factor : Factorization.factorizeNumber(g).toArray()) {
            int oddCnt = 0;
            for (int x : a) {
                oddCnt += x / factor % 2;
            }
            if (oddCnt == 1 && ans < factor) {
                ans = factor;
                odd = true;
            }
            if (oddCnt == 0 && ans < 2 * factor) {
                ans = 2 * factor;
                odd = false;
            }
        }
        out.println(ans);
        if (ans == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < a[i]; j++) {
                    out.append((char) (i + 'a'));
                }
            }
        } else {
            int k = odd ? ans : ans / 2;
            IntegerDeque dq = new IntegerDequeImpl((int) 1e5);
            for (int i = 0; i < n; i++) {
                if (a[i] / k % 2 == 1) {
                    dq.addLast(i);
                }
            }
            for (int i = 0; i < n; i++) {
                int time = a[i] / k;
                while (time >= 2) {
                    time -= 2;
                    dq.addLast(i);
                    dq.addFirst(i);
                }
            }
            StringBuilder piece = new StringBuilder((int) 1e5);

            for (IntegerIterator iterator = dq.iterator(); iterator.hasNext(); ) {
                piece.append((char) ('a' + iterator.next()));
            }
            StringBuilder rev = new StringBuilder(piece);
            rev.reverse();
            for (int i = 0; i < k; i++) {
                out.append(i % 2 == 0 ? piece : rev);
            }
        }
    }
}
