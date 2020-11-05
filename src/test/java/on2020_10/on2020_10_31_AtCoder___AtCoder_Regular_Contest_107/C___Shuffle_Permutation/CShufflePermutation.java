package on2020_10.on2020_10_31_AtCoder___AtCoder_Regular_Contest_107.C___Shuffle_Permutation;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class CShufflePermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[][] mat = new int[n][n];
        int[][] transpose = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transpose[j][i] = mat[i][j] = in.readInt();
            }
        }
        long ans = solve(mat, k) * solve(transpose, k) % mod;
        out.println(ans);
    }

    int mod = 998244353;

    public long fact(int n) {
        if (n == 0) {
            return 1;
        }
        return fact(n - 1) * n % mod;
    }

    public long solve(int[][] mat, int k) {
        int n = mat.length;
        DSUExt dsu = new DSUExt(n);
        dsu.init(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                boolean valid = true;
                for (int t = 0; t < n; t++) {
                    if (mat[i][t] + mat[j][t] > k) {
                        valid = false;
                    }
                }
                if (valid) {
                    dsu.merge(i, j);
                }
            }
        }
        long ans = 1;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                ans *= fact(dsu.size[i]);
            }
        }
        return ans;
    }
}

class DSUExt extends DSU {
    int[] size;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            size[i] = 1;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        size[a] += size[b];
    }
}