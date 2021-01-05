package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ExtremalPermutations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        if (m == 1) {
            out.println(0);
            return;
        }
        if (n == 1) {
            out.println(1);
            return;
        }
        int[] prev = new int[n];
        int[] next = new int[n];
        Arrays.fill(prev, 1);
        for (int i = 1; i < n; i++) {
            int remain = n - i;
            if (i % 2 == 1) {
                //inc
                int sum = 0;
                for (int j = 0; j <= remain; j++) {
                    sum += prev[j];
                    if (sum >= m) {
                        sum -= m;
                    }
                    next[j] = sum;
                }
            } else {
                //dec
                int sum = 0;
                for(int j = remain; j >= 0; j--){
                    next[j] = sum;
                    sum += prev[j];
                    if (sum >= m) {
                        sum -= m;
                    }
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = prev[0] * 2 % m;
        out.println(ans);
    }
}
