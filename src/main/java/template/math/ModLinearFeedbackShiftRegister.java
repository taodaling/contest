package template.math;

import template.primitve.generated.datastructure.IntegerArrayList;

/**
 * For given sequence a1, a2, ... , an.
 * <br>
 * find a linear feed back shift register c1, c2, ..., ck,
 * <br>
 * satisfy for any i > k that ai=\sum_{j=1}^k a_{i-j}c_j
 */
public class ModLinearFeedbackShiftRegister {
    private IntegerArrayList cm;
    int m = -1;
    int dm;
    private IntegerArrayList cn;
    private IntegerArrayList buf;
    private IntegerArrayList seq;
    private int mod;
    private Power pow;

    public ModLinearFeedbackShiftRegister(int mod, int cap) {
        cm = new IntegerArrayList(cap + 1);
        cn = new IntegerArrayList(cap + 1);
        seq = new IntegerArrayList(cap + 1);
        buf = new IntegerArrayList(cap + 1);
        cn.add(1);

        this.mod = mod;
        this.pow = new Power(mod);
    }

    public ModLinearFeedbackShiftRegister(int mod) {
        this(mod, 0);
    }

    private int estimateDelta() {
        int n = seq.size() - 1;
        long ans = 0;
        int[] cnData = cn.getData();
        int[] seqData = seq.getData();
        for (int i = 0, until = cn.size(); i < until; i++) {
            ans = (ans + (long) cnData[i] * seqData[n - i]) % mod;
        }
        return (int) ans;
    }

    public void add(int x) {
        x = DigitUtils.mod(x, mod);
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
        buf.addAll(cn);
        buf.expandWith(0, len + 1);

        long factor = (long) dn * pow.inverse(dm) % mod;

        int[] bufData = buf.getData();
        int[] cmData = cm.getData();
        for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
            bufData[i] = DigitUtils.mod(bufData[i] - factor * cmData[i - (n - m)], mod);
        }

        if (cn.size() < buf.size()) {
            IntegerArrayList tmp = cm;
            cm = cn;
            cn = tmp;
            m = n;
            dm = dn;
        }
        {
            IntegerArrayList tmp = cn;
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
    public int codeAt(int i) {
        return DigitUtils.negate(cn.get(i), mod);
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
            long ans = 0;
            int[] cnData = cn.getData();
            for (int i = 1, until = cn.size(); i < until; i++) {
                ans += (long) cnData[i] * get(len - i) % mod;
            }
            ans = DigitUtils.mod(-ans, mod);
            record((int) ans);
            return (int) ans;
        }
    }
}
