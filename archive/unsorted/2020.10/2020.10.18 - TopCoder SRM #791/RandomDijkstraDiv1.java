package contest;

import template.binary.Bits;
import template.math.IntRadix;
import template.math.KahamSummation;
import template.utils.Debug;

import java.util.Arrays;

public class RandomDijkstraDiv1 {
    IntRadix radix = new IntRadix(3);
    Debug debug = new Debug(false);
    public double solve(int N, int[] G) {
        int[][] mat = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mat[i][j] = G[i * N + j];
            }
        }
        int[] dists = new int[N];
        int[] pre = new int[N];
        boolean[] handled = new boolean[N];
        int[] post = new int[N];
        int inf = (int) 1e9;
        Arrays.fill(dists, inf);
        dists[0] = 0;
        for (int i = 0; i < N; i++) {
            int head = -1;
            for (int j = 0; j < N; j++) {
                if (handled[j]) {
                    continue;
                }
                if (head == -1 || dists[head] > dists[j]) {
                    head = j;
                }
            }
            handled[head] = true;
            for (int j = 0; j < N; j++) {
                int cand = mat[head][j] + dists[head];
                if (cand < dists[j]) {
                    dists[j] = cand;
                    pre[j] = 0;
                }
                if (cand == dists[j]) {
                    pre[j] |= 1 << head;
                }
            }
        }

        debug.debug("dist", dists);
//        for(int i = 0; i < N; i++){
//            debug.debug("i", i);
//            debug.debug("pre[i]", Integer.toBinaryString(pre[i]));
//            debug.debug("post[i]", Integer.toBinaryString(post[i]));
//        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Bits.get(pre[j], i) == 1) {
                    post[i] |= 1 << j;
                }
            }
        }

        double[] dp = new double[radix.set(0, N, 1)];
        dp[0] = 1;
        int[] bits = new int[3];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(bits, 0);
            for (int j = 0; j < N; j++) {
                bits[radix.get(i, j)] |= 1 << j;
            }
            int cand = Integer.bitCount(bits[0]);
            if (cand == 0) {
                continue;
            }
            double p = dp[i] / cand;
            for (int j = 0; j < N; j++) {
                if (Bits.get(bits[0], j) == 0) {
                    continue;
                }
                int next = i;
                if (j != 0 && (bits[2] & pre[j]) == 0) {
                    next = radix.set(next, j, 1);
                } else {
                    next = radix.set(next, j, 2);
                }
                dp[next] += p;
            }
        }

//        for(int i = 0; i < dp.length; i++){
//            debug.debug("i", i);
//            debug.debug("i", radix.toString(i));
//            debug.debug("dp[i]", dp[i]);
//        }
        KahamSummation ans = new KahamSummation();
        for (int i = 0; i < dp.length; i++) {
            boolean allRight = true;
            Arrays.fill(bits, 0);
            for (int j = 0; j < N; j++) {
                bits[radix.get(i, j)] |= 1 << j;
            }
            if(bits[0] != 0){
                continue;
            }
            for (int j = 0; j < N; j++) {
                if (j != 0 && (bits[2] & pre[j]) == 0) {
                    allRight = false;
                } else {
                }
            }

            if(allRight){
                ans.add(dp[i]);
            }
        }
        return ans.sum();
    }
}
