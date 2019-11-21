package template.polynomial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GravityLagrangeInterpolation {

    public GravityLagrangeInterpolation(int expect) {
        xs = new Polynomial(expect);
        ys = new Polynomial(expect);
        lx = new Polynomial(expect);
        lxBuf = new Polynomial(expect);
        invW = new Polynomial(expect);
        lx.setN(1);
        lx.coes[0] = 1;
    }

    /**
     * O(n)
     */
    public void addPoint(double x, double y) {
        Double exist = points.get(x);
        if (exist != null) {
            if (exist != y) {
                throw new RuntimeException();
            }
            return;
        }
        points.put(x, y);

        xs.setN(n + 1);
        xs.coes[n] = x;
        ys.setN(n + 1);
        ys.coes[n] = y;
        lx.multiply(-x, lxBuf);
        switchBuf();
        invW.setN(n + 1);
        invW.coes[n] = 1;
        for (int i = 0; i < n; i++) {
            invW.coes[i] *= xs.coes[i] - x;
            invW.coes[n] *= x - xs.coes[i];
        }
        n++;
    }

    /**
     * O(n)
     */
    public double getYByInterpolation(double x) {
        if (points.containsKey(x)) {
            return points.get(x);
        }

        double y = lx.function(x);
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double val = invW.coes[i] * (x - xs.coes[i]);
            val = ys.coes[i] / val;
            sum += val;
        }

        return y * sum;
    }

    /**
     * O(n^2)
     */
    public Polynomial preparePolynomial() {
        Polynomial ans = new Polynomial(n);
        Polynomial ansBuf = new Polynomial(n);
        for (int i = 0; i < n; i++) {
            double c = ys.coes[i] / invW.coes[i];
            lx.div(-xs.coes[i], ansBuf);
            ansBuf.mulConstant(c, ansBuf);
            ans.plus(ansBuf, ans);
        }
        return ans;
    }

    private void switchBuf() {
        Polynomial tmp = lx;
        lx = lxBuf;
        lxBuf = tmp;
    }


    Map<Double, Double> points = new HashMap<>();
    Polynomial xs;
    Polynomial ys;
    Polynomial lx;
    Polynomial lxBuf;
    Polynomial invW;
    int n;


    public class Polynomial {
        private double[] coes;
        private int n;

        public double getCoefficient(int i) {
            if (i >= n) {
                return 0;
            }
            return coes[i];
        }

        public int getRank() {
            return n - 1;
        }

        public double function(double x) {
            double ans = 0;
            double xi = 1;
            for (int i = 0; i < n; i++) {
                ans +=xi * coes[i];
                xi *= x;
            }
            return ans;
        }

        public Polynomial(int n) {
            this.n = 0;
            coes = new double[n];
        }

        public void ensureLength() {
            if (coes.length >= n) {
                return;
            }
            int proper = coes.length;
            while (proper < n) {
                proper = Math.max(2 * proper, proper + 10);
            }
            coes = Arrays.copyOf(coes, proper);
        }

        public void setN(int n) {
            this.n = n;
            ensureLength();
        }

        public void clear() {
            Arrays.fill(coes, 0, n, 0);
        }

        /**
         * this * (x + b) => ans
         */
        public void multiply(double b, Polynomial ans) {
            ans.setN(n + 1);
            for (int i = 0; i < n; i++) {
                ans.coes[i] = coes[i] * b;
            }
            ans.coes[n] = 0;
            for (int i = 0; i < n; i++) {
                ans.coes[i + 1] = ans.coes[i + 1] + coes[i];
            }
        }

        /**
         * this * b => ans
         */
        public void mulConstant(double b, Polynomial ans) {
            ans.setN(n);
            for (int i = 0; i < n; i++) {
                ans.coes[i] *= b;
            }
        }

        /**
         * this + a => ans
         */
        public void plus(Polynomial a, Polynomial ans) {
            ans.setN(Math.max(n, a.n));
            for (int i = 0; i < n; i++) {
                ans.coes[i] = coes[i];
            }
            for (int i = 0; i < a.n; i++) {
                ans.coes[i] += a.coes[i];
            }
        }

        /**
         * this / (x + b) => ans
         */
        public void div(double b, Polynomial ans) {
            ans.setN(n - 1);
            int affect = 0;
            for (int i = n - 1; i >= 1; i--) {
                affect += coes[i];
                ans.coes[i - 1] = affect;
                affect *= -b;
            }
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(coes, 0, n));
        }
    }

    @Override
    public String toString() {
        return points.toString();
    }
}
