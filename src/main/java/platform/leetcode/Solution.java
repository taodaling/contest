package platform.leetcode;


import java.util.*;
import java.util.stream.Collectors;

class Solution {
    public static void main(String[] args) {
        new Solution().maxNiceDivisors(73);
    }

    int mod = (int) 1e9 + 7;


    public long pow(long x, long k) {
        if (k == 0) {
            return 1;
        }
        long ans = pow(x, k / 2);
        ans = ans * ans % mod;
        if (k % 2 == 1) {
            ans = ans * x % mod;
        }
        return ans;
    }


    public int maxNiceDivisors(int primeFactors) {
        double e1 = Math.log(3) * (primeFactors / 3);
        double e2 = primeFactors < 2 ? -1e50 : (Math.log(3) * ((primeFactors - 2) / 3) + Math.log(2));
        double e3 = primeFactors < 4 ? -1e50 : (Math.log(3) * ((primeFactors - 4) / 3) + Math.log(2) * 2);
        long ans = 0;
        if(e1 >= e2 && e1 >= e3){
            ans = pow(3, primeFactors / 3);
        }else if(e2 >= e1 && e2 >= e3){
            ans = pow(3, ((primeFactors - 2) / 3)) * 2 % mod;
        }else{
            ans = pow(3, ((primeFactors - 4) / 3)) * 4 % mod;
        }
        return (int) ans;
    }
}