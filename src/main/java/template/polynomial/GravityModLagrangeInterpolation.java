package template.polynomial;

import template.math.Modular;
import template.math.Power;
import template.primitve.generated.IntegerList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GravityModLagrangeInterpolation {
    private Power power;
    private Modular modular;

    public GravityModLagrangeInterpolation(Modular modular, int expect) {
        this(new Power(modular), expect);
    }

    public GravityModLagrangeInterpolation(Power power, int expect) {
        this.modular = power.getModular();
        this.power = power;
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
    public void addPoint(int x, int y) {
        x = modular.valueOf(x);
        y = modular.valueOf(y);
        Integer exist = points.get(x);
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
        lx.multiply(modular.valueOf(-x), lxBuf);
        switchBuf();
        invW.setN(n + 1);
        invW.coes[n] = 1;
        for (int i = 0; i < n; i++) {
            invW.coes[i] = modular.mul(invW.coes[i], modular.subtract(xs.coes[i], x));
            invW.coes[n] = modular.mul(invW.coes[n], modular.subtract(x, xs.coes[i]));
        }
        n++;
    }

    /**
     * O(n)
     */
    public int getYByInterpolation(int x) {
        x = modular.valueOf(x);
        if (points.containsKey(x)) {
            return points.get(x);
        }

        int y = lx.function(x);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int val = modular.mul(invW.coes[i], modular.subtract(x, xs.coes[i]));
            val = modular.mul(power.inverseByFermat(val), ys.coes[i]);
            sum = modular.plus(sum, val);
        }

        return modular.mul(y, sum);
    }

    /**
     * O(n^2)
     */
    public Polynomial preparePolynomial() {
        Polynomial ans = new Polynomial(n);
        Polynomial ansBuf = new Polynomial(n);
        for (int i = 0; i < n; i++) {
            int c = modular.mul(ys.coes[i], power.inverseByFermat(invW.coes[i]));
            lx.div(modular.valueOf(-xs.coes[i]), ansBuf);
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


    Map<Integer, Integer> points = new HashMap<>();
    Polynomial xs;
    Polynomial ys;
    Polynomial lx;
    Polynomial lxBuf;
    Polynomial invW;
    int n;


    public class Polynomial {
        private int[] coes;
        private int n;

        public int[] toArray() {
            return Arrays.copyOf(coes, n);
        }

        public IntegerList toIntegerList() {
            IntegerList list = new IntegerList();
            list.addAll(coes, 0, n);
            return list;
        }

        public int getCoefficient(int i) {
            if (i >= n) {
                return 0;
            }
            return coes[i];
        }

        public int getRank() {
            return n - 1;
        }

        public int function(int x) {
            int ans = 0;
            int xi = 1;
            for (int i = 0; i < n; i++) {
                ans = modular.plus(ans, modular.mul(xi, coes[i]));
                xi = modular.mul(xi, x);
            }
            return ans;
        }

        public Polynomial(int n) {
            this.n = 0;
            coes = new int[n];
        }

        public void ensureLength() {
            if (coes.length >= n) {
                return;
            }
            int proper = coes.length;
            while (proper < n) {
                proper = Math.max(proper + proper, proper + 10);
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
        public void multiply(int b, Polynomial ans) {
            ans.setN(n + 1);
            for (int i = 0; i < n; i++) {
                ans.coes[i] = modular.mul(coes[i], b);
            }
            ans.coes[n] = 0;
            for (int i = 0; i < n; i++) {
                ans.coes[i + 1] = modular.plus(ans.coes[i + 1], coes[i]);
            }
        }

        /**
         * this * b => ans
         */
        public void mulConstant(int b, Polynomial ans) {
            ans.setN(n);
            for (int i = 0; i < n; i++) {
                ans.coes[i] = modular.mul(coes[i], b);
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
                ans.coes[i] = modular.plus(ans.coes[i], a.coes[i]);
            }
        }

        /**
         * this / (x + b) => ans
         */
        public void div(int b, Polynomial ans) {
            ans.setN(n - 1);
            int affect = 0;
            for (int i = n - 1; i >= 1; i--) {
                affect = modular.plus(affect, coes[i]);
                ans.coes[i - 1] = affect;
                affect = modular.mul(-affect, b);
            }
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(coes, 0, n));
        }
    }
}
