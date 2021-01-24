package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigFraction;

import java.math.BigInteger;
import java.util.Arrays;

public class HeadstrongStudent {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        a %= b;
        int[] occur = new int[b];
        Arrays.fill(occur, -1);
        occur[a] = 0;
        int step = 0;
        while (true) {
            step++;
            a = a * 10 % b;
            if (occur[a] != -1) {
                int ans1 = occur[a];
                int ans2 = step - occur[a];
                if (a == 0) {
                    ans2 = 0;
                }
                out.append(ans1).append(' ').append(ans2);
                return;
            }
            occur[a] = step;
        }
    }
}
