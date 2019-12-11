package on2019_12.on2019_12_11_.PermutationAndMultiplication;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class PermutationAndMultiplication {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int one = in.readInt();
        int zero = in.readInt();
        out.println(multiplyAndCount(one, zero));
    }

    int multiplyAndCount(int ones, int zeroes) {
        if(ones == 0){
            return 0;
        }

        int[] ans = new int[ones + zeroes + ones];
        for (int i = 0; i < ones; i++) {
            ans[i + ones + zeroes - 1] = 1;
        }
        int remain = 0;
        for (int i = 0; i < ones + zeroes + ones; i++) {
            int l = Math.max(0, i - (ones - 2));
            int r = Math.min(ones - 1, i);
            int provide = Math.max(0, r - l + 1);
            remain += provide + ans[i];
            ans[i] = remain % 2;
            remain /= 2;
        }

        int cnt = 0;
        for (int v : ans) {
            cnt += v;
        }
        return cnt;
    }
}
