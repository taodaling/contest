package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BFlipDigits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        char[] t = new char[n];
        in.rs(s);
        in.rs(t);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
            t[i] -= '0';
        }
        int left = 0;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (t[i] == 1) {
                left++;
                sum -= i;
            }
            if (s[i] == 1) {
                if (left == 0) {
                    left++;
                    sum -= i;
                } else {
                    left--;
                    sum += i;
                }
            }
        }
        if(left == 0){
            out.println(sum);
        }else{
            out.println(-1);
        }
    }
}
