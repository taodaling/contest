package on2020_04.on2020_04_21_TopCoder_SRM__761.SpanningSubgraphs;



import template.binary.Bits;
import template.binary.Log2;
import template.datastructure.DSU;
import template.math.Modular;
import template.math.Power;
import template.polynomial.ModGravityLagrangeInterpolation;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class SpanningSubgraphs {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);


    public int[] count(int n, int[] a, int[] b) {
        int[][] deg = new int[n][n];
        m = a.length;
        this.n = n;
        h = new int[1 << n];
        ps = new int[m + 1];

        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            deg[a[i]][b[i]]++;
            deg[b[i]][a[i]]++;
            dsu.merge(a[i], b[i]);
        }

        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != dsu.find(0)) {
                int[] ans = new int[m - n + 2];
                return ans;
            }
        }

        induce = new int[1 << n];
        for (int i = 1; i < induce.length; i++) {
            int lastBit = Integer.lowestOneBit(i);
            int last = i - lastBit;
            int log = Log2.floorLog(lastBit);
            induce[i] = induce[last] + deg[log][log];
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(last, j) == 0) {
                    continue;
                }
                induce[i] += deg[j][log] + deg[log][j];
            }
        }
        for (int i = 0; i < induce.length; i++) {
            induce[i] >>= 1;
        }

        // debug.debug("deg", deg);
        // debug.debug("induce", induce);

        ModGravityLagrangeInterpolation gli = new ModGravityLagrangeInterpolation(mod, m + 1);
        for (int i = 0; i <= m; i++) {
            int x = power.inverseByFermat(i + 1);
            gli.addPoint(x, prob(x));
        }
        IntegerList polynomial = gli.preparePolynomial().toIntegerList();

        IntegerList[] pi = new IntegerList[m + 1];
        IntegerList[] npi = new IntegerList[m + 1];
        pi[0] = new IntegerList();
        pi[0].add(1);
        npi[0] = pi[0];

        IntegerList x = new IntegerList(2);
        IntegerList y = new IntegerList(2);
        x.expandWith(0, 2);
        y.expandWith(0, 2);
        x.set(1, 1);
        y.set(0, 1);
        y.set(1, mod.valueOf(-1));

        for (int i = 1; i <= m; i++) {
            pi[i] = new IntegerList();
            Polynomials.mul(pi[i - 1], x, pi[i], mod);
            npi[i] = new IntegerList();
            Polynomials.mul(npi[i - 1], y, npi[i], mod);
        }

//        debug.debug("pi", pi);
//        debug.debug("npi", npi);
//        debug.debug("polynomial", polynomial);
        IntegerList mul = new IntegerList();
        IntegerList cache = new IntegerList();
        int[] ans = new int[m + 1];
        for (int i = m; i >= 0; i--) {
            int lowest = m - i;
            int factor = lowest >= polynomial.size() ? 0 : polynomial.get(lowest);
            Polynomials.mul(pi[m - i], npi[i], mul, mod);
//            debug.debug("factor", factor);
//            debug.debug("mul", mul);
//            debug.debug("polynomial", polynomial);
            Polynomials.mul(mul, factor, mod);
            Polynomials.subtract(polynomial, mul, cache, mod);
            {
                IntegerList tmp = cache;
                cache = polynomial;
                polynomial = tmp;
            }
            ans[i] = factor;
        }

        return Arrays.copyOfRange(ans, n - 1, ans.length);
    }


    int p;
    int[] induce;
    int n;
    int m;

    int[] h;
    int[] ps;

    public int between(int a, int b) {
        return induce[a | b] - induce[a] - induce[b];
    }

    public int h(int V) {
        if (h[V] == -1) {
            h[V] = 1;
            int subset = V;
            while (subset != 0) {
                subset = V & (subset - 1);
                int remove = mod.mul(h(subset), ps[between(subset + 1, V - subset)]);
                h[V] = mod.subtract(h[V], remove);
            }
        }
        return h[V];
    }

    public int prob(int p) {
        this.p = p;
        Arrays.fill(h, -1);
        ps[0] = 1;
        for (int i = 1; i <= m; i++) {
            ps[i] = mod.mul(ps[i - 1], p);
        }

        int ans = h((1 << n) - 2);
//
//        debug.debug("p", p);
//        debug.debug("ans", ans);
        return ans;
    }
}
