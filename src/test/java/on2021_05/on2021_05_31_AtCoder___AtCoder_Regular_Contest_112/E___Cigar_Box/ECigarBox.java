package on2021_05.on2021_05_31_AtCoder___AtCoder_Regular_Contest_112.E___Cigar_Box;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Power;
import template.utils.Debug;

import java.util.Arrays;

public class ECigarBox {
    int mod = 998244353;
    Combination comb = new Combination((int)1e5, mod);
    Power pow = new Power(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        long[] prevSeq = new long[n + 1];
        long[] nextSeq = new long[n + 1];
        prevSeq[0] = 1;
        for(int r = 0; r < m; r++){
            Arrays.fill(nextSeq, 0);
            for(int i = 0; i <= n; i++){
                nextSeq[i] += prevSeq[i] * i * 2 % mod;
                if(i + 1 <= n){
                    nextSeq[i + 1] += prevSeq[i];
                }
            }
            for(int i = 0; i <= n; i++){
                nextSeq[i] %= mod;
            }
            long[] tmp = prevSeq;
            prevSeq = nextSeq;
            nextSeq = tmp;
        }
        boolean[][] retainOk = new boolean[n][n];
        for(int i = 0; i < n; i++){
            retainOk[i][i] = true;
            for(int j = i + 1; j < n; j++){
                retainOk[i][j] = retainOk[i][j - 1] && a[j] > a[j - 1];
            }
        }

        debug.debug("prevSeq", prevSeq);
        long ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = i; j < n; j++){
                if(!retainOk[i][j]){
                    continue;
                }
                int seqLen = n - (j - i + 1);
                if(m < seqLen){
                    continue;
                }
                int before = i;
                int after = n - 1 - j;
                long way = comb.combination(before + after, before);
                way = way * prevSeq[before + after] % mod;
                ans += way;
                debug.debug("l", i);
                debug.debug("r", j);
                debug.debug("way", way);
            }
        }

        long way = pow.pow(2, n - 1);
        way = way * prevSeq[n] % mod;
        way = way * 2 % mod;
        ans += way;

        debug.debug("way", way);
        ans %= mod;
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
