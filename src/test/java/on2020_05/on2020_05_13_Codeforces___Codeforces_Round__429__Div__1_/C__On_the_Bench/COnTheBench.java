package on2020_05.on2020_05_13_Codeforces___Codeforces_Round__429__Div__1_.C__On_the_Bench;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class COnTheBench {
    Modular mod = new Modular(1e9 + 7);
    Factorial fact = new Factorial(1000, mod);
    Combination comb = new Combination(fact);

    Debug debug = new Debug(true);

    public int f(int i, int j) {
        int ans = mod.mul(fact.fact(i), comb.combination(i + j - 1 - j, j - 1));
        ans = mod.mul(ans, fact.invFact(j));
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        DSU dsu = new DSU(n);
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (square(a[j] * a[i])) {
                    dsu.merge(j, i);
                    break;
                }
            }
        }

        IntegerList list = new IntegerList();
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                list.add(dsu.size[dsu.find(i)]);
            }
        }

        int[] cnts = list.toArray();
        debug.debug("cnts", cnts);
        int m = cnts.length;
        int[][] dp = new int[m + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= m; i++) {
            int way = cnts[i - 1];
            for (int j = 0; j <= n; j++) {
                dp[i][j] = 0;
                for (int k = 1; k <= j && k <= way; k++) {
                    int contrib = mod.mul(f(way, k), dp[i - 1][j - k]);
                    dp[i][j] = mod.plus(dp[i][j], contrib);
                }
            }
        }
        debug.debug("dp", dp);

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            int local = mod.mul(dp[m][i], fact.fact(i));
            if ((n - i) % 2 == 1) {
                local = mod.valueOf(-local);
            }
            ans = mod.plus(ans, local);
        }

        out.println(ans);
    }

    public boolean square(long x) {
        long l = 1;
        long r = (long) 1e9;
        while (l < r) {
            long m = (l + r) / 2;
            if (m * m < x) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return l * l == x;
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    int[] size;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        size = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }


    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        size[a] += size[b];
        p[b] = a;
    }
}
