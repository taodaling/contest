package on2021_04.on2021_04_27_Codeforces___Codeforces_Round__196__Div__1_.C__Divisor_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongFactorization;
import template.math.LongPollardRho;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Set;

public class CDivisorTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.rl(n);
        Arrays.sort(a);
        SequenceUtils.reverse(a);
        divisor = new long[n];
        remain = new long[n];
        for (int i = 0; i < n; i++) {
            LongFactorization factorization = new LongFactorization(a[i]);
            for(long x : factorization.primes){
                long cur = a[i];
                while(cur % x == 0){
                    cur /= x;
                    divisor[i]++;
                }
            }
        }
        int[] p = new int[n];
        dfs(p, 0);
        out.println(best);
    }

    long best = (long) 1e18;
    long[] a;
    long[] remain;
    long[] divisor;

    void dfs(int[] p, int i) {
        if (i == p.length) {
            //check;
            System.arraycopy(a, 0, remain, 0, a.length);
            int neg = 0;
            long sum = 0;
            for (int j = 0; j < p.length; j++) {
                if (p[j] == -1) {
                    neg++;
                    sum += divisor[j];
                } else {
                    if (remain[p[j]] % a[j] != 0) {
                        return;
                    }
                    remain[p[j]] /= a[j];
                }
            }
            long cand = p.length + sum;
            if (neg > 1) {
                cand++;
            }
            for (int j = 0; j < p.length; j++) {
                if (remain[j] == a[j] && divisor[j] == 1) {
                    cand--;
                }
            }

            best = Math.min(best, cand);
            return;
        }

        for (int j = -1; j < i; j++) {
            p[i] = j;
            dfs(p, i + 1);
        }
    }
}
