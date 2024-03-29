package template.polynomial;

import template.math.DigitUtils;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModGravityLagrangeInterpolation {
    private Power power;
    private int mod;

    public ModGravityLagrangeInterpolation(int mod, int expect) {
        this(new Power(mod), expect);
    }

    public ModGravityLagrangeInterpolation(Power power, int expect) {
        this.mod = power.getMod();
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
        x = DigitUtils.mod(x, mod);
        y = DigitUtils.mod(y, mod);
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
        lx.multiply(DigitUtils.mod(-x, mod), lxBuf);
        switchBuf();
        invW.setN(n + 1);
        invW.coes[n] = 1;
        for (int i = 0; i < n; i++) {
            invW.coes[i] = DigitUtils.mod((long) invW.coes[i] * (xs.coes[i] - x), mod);
            invW.coes[n] = DigitUtils.mod((long) invW.coes[n] * (x - xs.coes[i]), mod);
        }
        n++;
    }

    /**
     * O(n)
     */
    public int getYByInterpolation(int x) {
        x = DigitUtils.mod(x, mod);
        if (points.containsKey(x)) {
            return points.get(x);
        }

        int y = lx.function(x);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            int val = DigitUtils.mod((long) invW.coes[i] * (x - xs.coes[i]), mod);
            val = (int) ((long) power.inverse(val) * ys.coes[i] % mod);
            sum += val;
        }

        sum %= mod;
        return (int) (y * sum % mod);
    }

    /**
     * O(n^2)
     */
    public Polynomial preparePolynomial() {
        Polynomial ans = new Polynomial(n);
        Polynomial ansBuf = new Polynomial(n);
        for (int i = 0; i < n; i++) {
            long c = (long) ys.coes[i] * power.inverse(invW.coes[i]) % mod;
            lx.div(DigitUtils.mod(-xs.coes[i], mod), ansBuf);
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


    @Override
    public String toString() {
        return preparePolynomial().toString();
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

        public IntegerArrayList toIntegerList() {
            IntegerArrayList list = new IntegerArrayList();
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
            long xi = 1;
            for (int i = 0; i < n; i++) {
                ans = (int) ((ans + xi * coes[i]) % mod);
                xi = xi * x % mod;
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
        public void multiply(long b, Polynomial ans) {
            ans.setN(n + 1);
            for (int i = 0; i < n; i++) {
                long sum = coes[i] * b;
                if (i > 0) {
                    sum += coes[i - 1];
                }
                ans.coes[i] = (int) (sum % mod);
            }
            ans.coes[n] = coes[n - 1];
        }

        /**
         * this * b => ans
         */
        public void mulConstant(long b, Polynomial ans) {
            ans.setN(n);
            for (int i = 0; i < n; i++) {
                ans.coes[i] = (int) (coes[i] * b % mod);
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
                ans.coes[i] = (ans.coes[i] + a.coes[i]) % mod;
            }
        }

        /**
         * this / (x + b) => ans
         */
        public void div(long b, Polynomial ans) {
            ans.setN(n - 1);
            int affect = 0;
            for (int i = n - 1; i >= 1; i--) {
                affect = (affect + coes[i]) % mod;
                ans.coes[i - 1] = affect;
                affect = DigitUtils.mod(-affect * b, mod);
            }
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(coes, 0, n));
        }
    }
}
