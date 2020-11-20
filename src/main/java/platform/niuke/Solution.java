package platform.niuke;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Solution {
    public void solve() {
        try {
            Thread t = new Thread(null, () -> {
            }, "", 1 << 29);
            t.start();
            t.join();
        } catch (Exception e) {
        }
    }

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     * 返回1-n的所有k*m的和
     * @param num long长整型 正整数 n
     * @return long长整型
     */
    public long cowModCount (long n) {
        // write code here
        int mod = (int)1e9 + 7;
        long sum = 0;
        for(long p = 1, r; p <= n; p = r){
            r = n / (n / p);
            r = Math.min(r, n);
            long v = n / p;
            sum += v * n % mod - (r + p) * (r - p + 1) / 2 % mod * v % mod * v % mod;
        }
        sum %= mod;
        if(sum < 0){
            sum += mod;
        }
        return sum;
    }
}

