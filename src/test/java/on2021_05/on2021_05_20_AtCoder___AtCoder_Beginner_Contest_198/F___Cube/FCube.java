package on2021_05.on2021_05_20_AtCoder___AtCoder_Beginner_Contest_198.F___Cube;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Group;
import template.math.ModMatrix;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.ArrayIndex;
import template.utils.Debug;

import java.util.Arrays;
import java.util.stream.IntStream;

public class FCube {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Group<Perm> g = new Group<>(false, new Perm(IntStream.range(0, 6).toArray()), (a, b) -> new Perm(merge(a.p, b.p)));
        g.add(new Perm(4, 5, 2, 3, 1, 0));
        g.add(new Perm(2, 3, 1, 0, 4, 5));

        long S = in.rl() - 6;
        long sum = 0;
        for (Perm p : g) {
            int[] info = extractParameter(p.p);
            long contrib = way(info, S);
            sum += contrib;
        }

        sum %= mod;
        long divG = DigitUtils.modInverse(g.size(), mod);
        long ans = sum * divG % mod;
        out.println(ans);
    }

    Debug debug = new Debug(false);
    int mod = 998244353;

    //how many ways exists that \sum_i ai * xi = m
    long way(int[] a, long m) {
        int max = Arrays.stream(a).max().getAsInt();
        int n = a.length;
        int[][] mat = new int[max * n][max * n];
        ArrayIndex ai = new ArrayIndex(max, n);
        for (int i = 1; i < max; i++) {
            for (int j = 0; j < n; j++) {
                mat[ai.indexOf(i - 1, j)][ai.indexOf(i, j)] = 1;
            }
        }
        for (int j = 0; j < n; j++) {
            for (int state = 0; state <= j; state++) {
                mat[ai.indexOf(max - 1, j)][ai.indexOf(max - 1 - a[j] + 1, state)] = 1;
            }
        }
        ModMatrix A = new ModMatrix((i, j) -> mat[i][j], max * n, max * n);
        int[] initVec = new int[max * n];
        initVec[ai.indexOf(max - 1, 0)] = 1;
        ModMatrix Am = ModMatrix.pow(A, m, mod);
        ModMatrix x = new ModMatrix(initVec, 1);
        ModMatrix Amx = ModMatrix.mul(Am, x, mod);

        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Amx.get(ai.indexOf(max - 1, i), 0);
        }

        ans %= mod;
        return ans;
    }

    int[] extractParameter(int[] p) {
        IntegerArrayList res = new IntegerArrayList(p.length);
        int n = p.length;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                continue;
            }
            int cnt = 0;
            int go = i;
            while (!visited[go]) {
                visited[go] = true;
                go = p[go];
                cnt++;
            }
            res.add(cnt);
        }

        return res.toArray();
    }

    //a * b
    int[] merge(int[] a, int[] b) {
        int n = a.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = b[a[i]];
        }
        return ans;
    }
}

class Perm {
    int[] p;

    public Perm(int... p) {
        this.p = p;
    }

    @Override
    public boolean equals(Object obj) {
        Perm operand = (Perm) obj;
        return Arrays.equals(p, operand.p);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(p);
    }

    @Override
    public String toString() {
        return Arrays.toString(p);
    }
}

