package on2021_11.on2021_11_02_Codeforces___Codeforces_Round__752__Div__1_.D__Artistic_Partition;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

public class DArtisticPartition {
    int K = 20;
    int N = (int) 1e5;
    long[][] dp = new long[K][N + 1];
    int[][] pts = new int[N + 1][];
    long[][] suffix = new long[N + 1][];
    int[][] start = new int[N + 1][];
    Cost cost;
    long inf = (long) 1e18;
    long[] p;

    class Cost {
        int r;
        int l;
        long cost;

        public void reset(int L, int R) {
            r = R;
            l = L;

            if (l > r) {
                cost = 0;
                return;
            }

            int high = BinarySearch.upperBound(start[r], 0, start[r].length - 1, l);
            assert high > 0;
            cost = 0;
            int last = r + 1;
            if (high < suffix[r].length) {
                cost += suffix[r][high];
                last = start[r][high];
            }
            cost += (last - l) * p[pts[r][high - 1]];
        }

        public void move(int L) {
            while (l < L) {
                cost -= p[r / l];
                l++;
            }
            while (l > L) {
                l--;
                cost += p[r / l];
            }
        }
    }

    public void preprocess() {
        IntegerArrayList ptBuf = new IntegerArrayList(N + 1);
        IntegerArrayList startBuf = new IntegerArrayList(N + 1);
        for (int i = 1; i <= N; i++) {
            ptBuf.clear();
            startBuf.clear();
            for (int j = 1, r; j <= i; j = r + 1) {
                int v = i / j;
                r = i / v;
                ptBuf.add(v);
                startBuf.add(j);
            }
            pts[i] = ptBuf.toArray();
            start[i] = startBuf.toArray();
            int m = pts[i].length;
            suffix[i] = new long[m];
            long last = i + 1;
            long lastSum = 0;
            for (int j = m - 1; j >= 0; j--) {
                suffix[i][j] = lastSum + (last - start[i][j]) * p[pts[i][j]];
                last = start[i][j];
                lastSum = suffix[i][j];
            }
        }
    }

    public void dac(int level, int l, int r, int L, int R) {
        if (l > r) {
            return;
        }
        int m = (l + r) >>> 1;

        int start = Math.min(m, R);
        cost.reset(start + 1, m);

        int bestChoice = -1;
        long bestCost = inf;
        for (int i = start; i >= L; i--) {
            cost.move(i + 1);
            long cand = cost.cost + dp[level - 1][i];
            if (cand < bestCost) {
                bestCost = cand;
                bestChoice = i;
            }
        }

        dp[level][m] = bestCost;
        dac(level, l, m - 1, L, bestChoice);
        dac(level, m + 1, r, bestChoice, R);
    }

    public DArtisticPartition() {
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(N);
        int[] euler = sieve.getEuler();
        p = new long[N + 1];
        p[0] = euler[0];
        for (int i = 1; i <= N; i++) {
            p[i] = p[i - 1] + euler[i];
        }
        preprocess();
        cost = new Cost();
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 1; i < K; i++) {
            dac(i, 1, N, 0, N);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        if(k >= K){
            out.println(n);
            return;
        }
        long ans = dp[k][n];
        out.println(ans);
    }
}

