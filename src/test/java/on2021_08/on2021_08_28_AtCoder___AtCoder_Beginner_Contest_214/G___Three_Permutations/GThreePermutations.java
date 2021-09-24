package on2021_08.on2021_08_28_AtCoder___AtCoder_Beginner_Contest_214.G___Three_Permutations;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class GThreePermutations {
    int mod = (int) 1e9 + 7;
    int L = (int) 1e4;
    Factorial fact = new Factorial(L, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = new int[n];
        int[] q = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
        }
        for (int i = 0; i < n; i++) {
            q[i] = in.ri() - 1;
        }
        int[] pq = new int[n];
        for (int i = 0; i < n; i++) {
            pq[p[i]] = q[i];
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < n; i++) {
            dsu.merge(i, pq[i]);
        }
        long[] globalPrev = new long[n + 1];
        long[] globalNext = new long[n + 1];
        long[][][] localPrev = new long[2][2][n + 1];
        long[][][] localNext = new long[2][2][n + 1];
        globalPrev[0] = 1;
        int globalSize = 0;
        for (int c = n - 1; c >= 0; c--) {
            if (dsu.find(c) != c) {
                continue;
            }
            int size = dsu.size[c];
            SequenceUtils.deepFill(localPrev, 0L);
            if (size > 1) {
                localPrev[0][1][1] = 1;
                localPrev[1][0][1] = 1;
                localPrev[0][0][0] = 1;
            }else{
                localPrev[0][0][1] = 1;
                localPrev[0][0][0] = 1;
            }
            for (int r = 2; r <= size; r++) {
                SequenceUtils.deepFill(localNext, 0L);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int t = 0; t <= n; t++) {
                            long way = localPrev[i][j][t];
                            if (way == 0) {
                                continue;
                            }
                            //not add
                            localNext[i][0][t] += way;
                            //add L
                            if (j == 0) {
                                localNext[i][0][t + 1] += way;
                            }
                            //add R
                            localNext[i][1][t + 1] += way;
                        }
                    }
                }
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (int t = 0; t <= n; t++) {
                            localNext[i][j][t] = DigitUtils.modWithoutDivision(localNext[i][j][t], mod);
                        }
                    }
                }
                long[][][] tmp = localPrev;
                localPrev = localNext;
                localNext = tmp;
            }
            debug.debug("size", size);
            debug.debug("localPrev", localPrev);
            //merge
            SequenceUtils.deepFill(globalNext, 0L);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int t = 0; t <= size; t++) {
                        long way = localPrev[i][j][t];
                        if (i == 1 && j == 1 || way == 0) {
                            continue;
                        }
                        for (int z = 0; z <= globalSize; z++) {
                            globalNext[t + z] += way * globalPrev[z] % mod;
                        }
                    }
                }
            }
            globalSize += size;
            for (int i = 0; i <= n; i++) {
                globalNext[i] %= mod;
            }
            long[] tmp = globalPrev;
            globalPrev = globalNext;
            globalNext = tmp;
            debug.debug("globalPrev", globalPrev);
            debug.debug("size", size);
        }
        long ans = 0;
        for (int i = 0; i <= n; i++) {
            long contrib = globalPrev[i];
            contrib = contrib * fact.fact(n - i) % mod;
            if (i % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
