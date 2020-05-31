package on2020_05.on2020_05_31_AtCoder___NOMURA_Programming_Competition_2020.D___Urban_Planning;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Factorial;
import template.math.Factorization;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class DUrbanPlanning {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        DSU dsu = new DSU(n);
        int[] p = new int[n];
        Modular mod = new Modular(1e9 + 7);
        CachedPow pow = new CachedPow(n - 1, mod);
        Factorial fact = new Factorial(n, mod);
        int k = 0;
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
            if (p[i] > 0) {
                p[i]--;
            } else {
                k++;
            }
        }

        int total = mod.mul(n, pow.pow(k));
        int remove = 0;
        debug.debug("total", total);
        for (int i = 0; i < n; i++) {
            if (p[i] == -1) {
                continue;
            }
            if (dsu.find(i) == dsu.find(p[i])) {
                remove = mod.plus(remove, pow.pow(k));
                continue;
            }
            dsu.merge(i, p[i]);
        }
        debug.debug("redundant", remove);


        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (p[i] == -1) {
                list.add(dsu.size[dsu.find(i)]);
            }
        }

        int m = list.size();
        int[][] dp = new int[m + 1][m + 1];
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            int size = list.get(i);

            //as part of cycle
            for (int j = 0; j < m; j++) {
                dp[i + 1][j + 1] = mod.plus(dp[i + 1][j + 1], mod.mul(dp[i][j], size));
            }

            //as other part
            for (int j = 0; j <= m; j++) {
                dp[i + 1][j] = mod.plus(dp[i + 1][j], mod.mul(dp[i][j], n - 1));
            }
        }

        int circle = 0;
        for (int i = 2; i <= m; i++) {
            int way = fact.fact(i - 1);
            circle = mod.plus(circle, mod.mul(way, dp[m][i]));
        }
        debug.debug("circle", circle);
        //self circle
        int self = 0;
        for (int i = 0; i < m; i++) {
            int size = list.get(i);
            self = mod.plus(self, mod.mul(size - 1, pow.pow(k - 1)));
        }
        debug.debug("self", self);
        remove = mod.plus(remove, self);
        remove = mod.plus(remove, circle);
        int ans = mod.subtract(total, remove);
        out.println(ans);
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
