package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BLittlePonyAndHarmonyChest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        EulerSieve sieve = new EulerSieve(60);
        int[] indices = new int[60];
        for (int i = 0; i < sieve.getPrimeCount(); i++) {
            indices[sieve.get(i)] = i;
        }
        int m = sieve.getPrimeCount();
        int[] bits = new int[60];
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime(60);
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 1; i < 60; i++) {
            list.clear();
            list.addAll(stack.getStack(i).iterator());
            for (int p : list.toArray()) {
                bits[i] |= 1 << indices[p];
            }
        }

        int n = in.ri();
        int[][] dp = new int[n + 1][1 << m];
        int[][] prev = new int[n + 1][1 << m];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            for (int j = 0; j < 1 << m; j++) {
                for (int k = 1; k < 60; k++) {
                    if ((j & bits[k]) != 0) {
                        continue;
                    }
                    int cost = Math.abs(k - a);
                    int to = j | bits[k];
                    if (dp[i + 1][to] > cost + dp[i][j]) {
                        dp[i + 1][to] = cost + dp[i][j];
                        prev[i + 1][to] = k;
                    }
                }
            }
        }
        int bestIndex = 0;
        for (int i = 0; i < 1 << m; i++) {
            if (dp[n][i] < dp[n][bestIndex]) {
                bestIndex = i;
            }
        }
        IntegerArrayList seq = new IntegerArrayList(n);
        int now = bestIndex;
        for (int i = n; i >= 1; i--) {
            seq.add(prev[i][now]);
            now ^= bits[prev[i][now]];
        }
        seq.reverse();
        for(int x : seq.toArray()){
            out.append(x).append(' ');
        }
    }
}
