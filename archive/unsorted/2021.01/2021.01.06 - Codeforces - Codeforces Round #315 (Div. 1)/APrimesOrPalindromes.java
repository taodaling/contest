package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

public class APrimesOrPalindromes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        EulerSieve es = new EulerSieve((int) 1e7);
        int index = -1;
        long p = in.ri();
        long q = in.ri();
        int a = 0;
        int b = 0;
        int[] buf = new int[10];
        for (int i = 1; i <= (int) 1e7; i++) {
            if (es.isPrime(i)) {
                a++;
            }
            int len = 0;
            int x = i;
            while (x > 0) {
                buf[len++] = x % 10;
                x /= 10;
            }
            int l = 0;
            int r = len - 1;
            boolean valid = true;
            while (l < r) {
                if (buf[l] != buf[r]) {
                    valid = false;
                }
                l++;
                r--;
            }
            if (valid) {
                b++;
            }
            if (q * a <= p * b) {
                index = i;
            }
        }

        if(index == -1){
            out.println("Palindromic tree is better than splay tree");
        }else{
            out.println(index);
        }
    }
}
