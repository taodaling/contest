package on2021_05.on2021_05_23_Luogu.P2144__FJOI2007_____;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class P2144FJOI2007 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        BigInteger[] dp = new BigInteger[n + 1];
        BigInteger[] A = new BigInteger[n + 1];
        BigInteger[] B = new BigInteger[n + 1];
        dp[0] = A[0] = BigInteger.ONE;
        B[0] = BigInteger.ZERO;
        for(int i = 1; i <= n; i++){
            dp[i] = A[i - 1].multiply(BigInteger.valueOf(i)).subtract(B[i - 1]);
            A[i] = A[i - 1].add(dp[i]);
            B[i] = B[i - 1].add(dp[i].multiply(BigInteger.valueOf(i)));
        }
        BigInteger sum = BigInteger.ZERO;
        for(int i = 1; i <= n; i++){
            BigInteger contrib = dp[n - i].multiply(BigInteger.valueOf(i).pow(2));
            sum = sum.add(contrib);
        }
        out.println(sum);
    }


}
