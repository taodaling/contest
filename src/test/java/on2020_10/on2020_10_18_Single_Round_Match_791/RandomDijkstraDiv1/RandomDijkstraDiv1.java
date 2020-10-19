package on2020_10.on2020_10_18_Single_Round_Match_791.RandomDijkstraDiv1;



import template.binary.Bits;
import template.math.IntRadix;
import template.math.Permutation;
import template.math.PermutationUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RandomDijkstraDiv1 {
    IntRadix radix = new IntRadix(3);
    Debug debug = new Debug(true);

    public double solve(int N, int[] G) {
        int[][] mat = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mat[i][j] = G[i * N + j];
            }
        }

        int inf = (int) 1e9;
        int[] dists = new int[N];
        Arrays.fill(dists, inf);
        dists[0] = 0;
        int[] pre = new int[N];
        int[] post = new int[N];
        boolean[] handled = new boolean[N];
        for (int i = 0; i < N; i++) {
            int head = -1;
            for (int j = 0; j < N; j++) {
                if (handled[j]) {
                    continue;
                }
                if (head == -1 || dists[j] < dists[head]) {
                    head = j;
                }
            }
            handled[head] = true;
            for (int j = 0; j < N; j++) {
                if (j == head) {
                    continue;
                }
                int cand = dists[head] + mat[head][j];
                if (dists[j] > cand) {
                    dists[j] = cand;
                    pre[j] = 0;
                }
                if (dists[j] == cand) {
                    pre[j] |= 1 << head;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Bits.get(pre[j], i) == 1) {
                    post[i] |= 1 << j;
                }
            }
        }

        double[] dp = new double[radix.set(0, N, 1)];
        dp[0] = 1;
        int set = 1;
        int rightSet = 2;
        int[] bits = new int[3];
        int[][] val = new int[3][1 << N];
        for (int i = 1; i <= 2; i++) {
            for (int j = 0; j < 1 << N; j++) {
                for (int k = 0; k < N; k++) {
                    if (Bits.get(j, k) == 0) {
                        continue;
                    }
                    val[i][j] = radix.set(val[i][j], k, i);
                }
            }
        }

        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(bits, 0);
            for (int j = 0; j < N; j++) {
                bits[radix.get(i, j)] |= 1 << j;
            }
            int zeroCnt = Integer.bitCount(bits[0]);
            if (zeroCnt == 0) {
                continue;
            }
            double prob = 1.0d / zeroCnt * dp[i];
            if (prob == 0) {
                continue;
            }
            for (int j = 0; j < N; j++) {
                if (Bits.get(bits[0], j) == 0) {
                    continue;
                }
                if ((bits[2] & pre[j]) == 0 && j != 0) {
                    int next = radix.set(i, j, 1);
                    dp[next] += prob;
                } else {
                    int next = radix.set(i, j, 2);
                    int inter = bits[1] & post[j];
                    next = next - val[1][inter] + val[2][inter];
                    dp[next] += prob;
                }
            }
        }

        double ans = dp[dp.length - 1];
        double[] bf = bf(mat, dists);
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] == 0) {
                continue;
            }
            debug.debug("i", radix.toString(i));
            debug.debug("dp[i]", dp[i]);
            if(Math.abs(bf[i] - dp[i]) > 1e-10){
                throw new RuntimeException();
            }
        }
        return ans;
    }

    public double[] bf(int[][] mat, int[] right) {
        int N = right.length;
        int[] perm = IntStream.range(0, N).toArray();
        double[] prob = new double[radix.set(0, N, 1)];
        int m = 0;
        do {
            m++;
            int inf = (int) 1e9;
            int[] dists = new int[N];
            Arrays.fill(dists, inf);
            dists[0] = 0;
            boolean[] handled = new boolean[N];
            for (int i = 0; i < N; i++) {
                int head = perm[i];
                handled[head] = true;
                for (int j = 0; j < N; j++) {
                    if (j == head) {
                        continue;
                    }
                    int cand = dists[head] + mat[head][j];
                    if (dists[j] > cand) {
                        dists[j] = cand;
                    }
                }

                int now = 0;
                for (int k = 0; k < N; k++) {
                    if (handled[k]) {
                        if (dists[k] == right[k]) {
                            now = radix.set(now, k, 2);
                        } else {
                            now = radix.set(now, k, 1);
                        }
                    }
                }
                prob[now] += 1;
                if(now == 17){
                    debug.debug("perm", Arrays.toString(perm));
                }
            }


        } while (PermutationUtils.nextPermutation(perm));
        for (int i = 0; i < prob.length; i++) {
            prob[i] /= m;
        }
        prob[0] = 1;
        return prob;
    }
}
