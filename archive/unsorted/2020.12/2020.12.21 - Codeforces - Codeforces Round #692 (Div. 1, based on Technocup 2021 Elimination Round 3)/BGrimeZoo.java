package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BGrimeZoo {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.rs(s, 1);
        long a = in.ri();
        long b = in.ri();
        if (a < b) {
            SequenceUtils.reverse(s, 1, n);
            long tmp = a;
            a = b;
            b = tmp;
        }
        //always 1,0
        IntegerBIT bit = new IntegerBIT(n);
        for (int i = 1; i <= n; i++) {
            if (s[i] != '1') {
                bit.update(i, 0);
            } else {
                bit.update(i, 1);
            }
        }
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            if (s[i] != '1') {
                sum += b * bit.query(i - 1) + a * bit.query(i + 1, n);
            }
        }
        long ans = sum;
        for (int i = 1; i <= n; i++) {
            if (s[i] == '?') {
                sum -= b * bit.query(i - 1) + a * bit.query(i + 1, n);
                bit.update(i, 1);
                sum += (i - 1 - bit.query(i - 1)) * a + b * (n - i - bit.query(i + 1, n));
                ans = Math.min(ans, sum);
            }
        }
        out.println(ans);
    }
}


