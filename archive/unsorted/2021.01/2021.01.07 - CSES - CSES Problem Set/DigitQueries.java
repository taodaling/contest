package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DigitQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        for(int i = 0; i < q; i++){
            out.println(solve(in.rl()));
        }
    }

    public int solve(long k) {
        k--;
        long l = 1;
        long r = l * 10 - 1;
        int eachLen = 1;
        for (; ; l = l * 10, r = l * 10 - 1, eachLen++) {
            long totalLen = eachLen * (r - l + 1);
            if (totalLen <= k) {
                k -= totalLen;
                continue;
            }
            long skip = k / eachLen;
            k %= eachLen;
            long pick = l + skip;
            return String.valueOf(pick).charAt((int) k) - '0';
        }
    }
}
