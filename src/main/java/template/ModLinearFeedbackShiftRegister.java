package template;

/**
 * For given sequence a1, a2, ... , an.
 * <br>
 * find a linear feed back shift register c1, c2, ..., ck,
 * <br>
 * satisfy for any i > k that ai=\sum_{j=1}^k a_{i-j}c_j
 */
public class ModLinearFeedbackShiftRegister {
    private IntList cm;
    int m = -1;
    int dm;
    private IntList cn;
    private IntList buf;
    private IntList seq;
    private NumberTheory.Modular mod;
    private NumberTheory.Power pow;

    public ModLinearFeedbackShiftRegister(NumberTheory.Modular mod, int cap) {
        cm = new IntList(cap + 1);
        cn = new IntList(cap + 1);
        seq = new IntList(cap + 1);
        buf = new IntList(cap + 1);
        cn.add(1);

        this.mod = mod;
        this.pow = new NumberTheory.Power(mod);
    }

    public ModLinearFeedbackShiftRegister(NumberTheory.Modular mod) {
        this(mod, 0);
    }

    private int estimateDelta() {
        int n = seq.size() - 1;
        int ans = 0;
        for (int i = 0, until = cn.size(); i < until; i++) {
            ans = mod.plus(ans, mod.mul(cn.get(i), seq.get(n - i)));
        }
        return ans;
    }

    public void add(int x) {
        x = mod.valueOf(x);
        int n = seq.size();

        seq.add(x);
        int dn = estimateDelta();
        if (dn == 0) {
            return;
        }

        if (m < 0) {
            cm.clear();
            cm.addAll(cn);
            dm = dn;
            m = n;

            cn.expandWith(0, n + 2);
            return;
        }

        int ln = cn.size() - 1;
        int len = Math.max(ln, n + 1 - ln);
        buf.clear();
        buf.expandWith(0, len + 1);
        for (int i = 0, until = cn.size(); i < until; i++) {
            buf.set(i, cn.get(i));
        }

        int factor = mod.mul(dn, pow.inverse(dm));
        for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
            buf.set(i, mod.subtract(buf.get(i), mod.mul(factor, cm.get(i - (n - m)))));
        }

        if (cn.size() < buf.size()) {
            IntList tmp = cm;
            cm = cn;
            cn = tmp;
            m = n;
            dm = dn;
        }
        {
            IntList tmp = cn;
            cn = buf;
            buf = tmp;
        }


    }

    public int length() {
        return cn.size() - 1;
    }

    @Override
    public String toString() {
        return cn.toString();
    }

    public Estimator newEstimator(int... init) {
        if (init.length != length()) {
            throw new IllegalArgumentException();
        }
        if (length() == 0) {
            return new ZeroEstimator();
        }
        return new EstimatorImpl(init);
    }

    public interface Estimator {
        int next();
    }

    private class ZeroEstimator implements Estimator {
        @Override
        public int next() {
            return 0;
        }
    }

    //start from 1
    public int codeAt(int i){
        return cn.get(i);
    }

    private class EstimatorImpl implements Estimator {
        int[] seq;
        int offset;
        int len;

        private EstimatorImpl(int[] seq) {
            this.seq = seq.clone();
            offset = 0;
            len = seq.length;
        }

        private int get(int i) {
            return seq[(i + offset) % len];
        }

        private void record(int x) {
            seq[offset++] = x;
            if (offset >= len) {
                offset = 0;
            }
        }

        public int next() {
            int ans = 0;
            for (int i = 1, until = cn.size(); i < until; i++) {
                ans = mod.plus(ans, mod.mul(cn.get(i), get(len - i)));
            }
            ans = mod.subtract(0, ans);
            record(ans);
            return ans;
        }
    }
}
