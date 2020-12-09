package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BMany110 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long copy = (long) 1e10;
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        int firstZero = 0;
        while (firstZero < n && s[firstZero] == '1') {
            firstZero++;
        }
        if (firstZero >= 3) {
            out.println(0);
            return;
        }
        if (firstZero == n) {
            if (n == 1) {
                out.println(2 * copy);
            }else{
                out.println(copy);
            }
            return;
        }
        String template = "011";
        for (int i = firstZero; i < n; i++) {
            if (s[i] != template.charAt((i - firstZero) % 3)) {
                out.println(0);
                return;
            }
        }
        int zeroCnt = DigitUtils.ceilDiv(n - firstZero, 3);
        long possible = copy - zeroCnt + 1;
        if (s[n - 1] != '0') {
            possible--;
        }
        out.println(possible);
    }
}
