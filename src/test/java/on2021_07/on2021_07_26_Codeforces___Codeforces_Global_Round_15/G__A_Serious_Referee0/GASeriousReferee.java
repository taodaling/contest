package on2021_07.on2021_07_26_Codeforces___Codeforces_Global_Round_15.G__A_Serious_Referee0;



import template.binary.Bits;
import template.binary.FastBitCount2;
import template.io.FastInput;
import template.io.FastOutput;

public class GASeriousReferee {
    int n;
    long[] S;
    long[][] sortedS;
    int[] delta;
    boolean ok = true;
    long whole;

    public void dfs(int i, long bitset) {
        if (!ok) {
            return;
        }
        if (i == S.length) {
            int bc = Long.bitCount(bitset);
            if (bitset != (~Bits.headLongMask(n - bc) & whole)) {
                ok = false;
            }
            return;
        }
        int cur = Long.bitCount(bitset & S[i]);
        long cleanBitset = bitset & ~S[i];
        for (int j = 0; j <= delta[i]; j++) {
            dfs(i + 1, cleanBitset | sortedS[i][j + cur]);
        }
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        whole = (1L << n) - 1;
        int k = in.ri();
        S = new long[k];
        sortedS = new long[k][];
        delta = new int[k];
        long prev = 0;
        for (int i = 0; i < k; i++) {
            int q = in.ri();
            for (int j = 0; j < q; j++) {
                int index = in.ri() - 1;
                S[i] |= 1L << index;
            }
            sortedS[i] = new long[q + 1];
            long v = S[i];
            for (int j = 1; j <= q; j++) {
                sortedS[i][j] = sortedS[i][j - 1];
                long highest = Long.highestOneBit(v);
                v -= highest;
                sortedS[i][j] |= highest;
            }
            delta[i] = Long.bitCount(S[i] ^ (S[i] & prev));
            prev |= S[i];
        }
        if(prev != whole && n > 1){
            ok = false;
        }
        dfs(0, 0);

        if (ok) {
            out.println("ACCEPTED");
        } else {
            out.println("REJECTED");
        }
    }
}
