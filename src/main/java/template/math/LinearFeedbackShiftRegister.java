package template.math;

import template.primitve.generated.datastructure.DoubleArrayList;

public class LinearFeedbackShiftRegister {
    private DoubleArrayList cm;
    private int m = -1;
    private double dm;
    private DoubleArrayList cn;
    private DoubleArrayList buf;
    private DoubleArrayList seq;
    private double prec;
    private KahamSummation summation = new KahamSummation();

    public LinearFeedbackShiftRegister(int cap, double prec) {
        this.prec = prec;
        cm = new DoubleArrayList(cap + 1);
        cn = new DoubleArrayList(cap + 1);
        seq = new DoubleArrayList(cap + 1);
        buf = new DoubleArrayList(cap + 1);

        cn.add(1);
    }

    public LinearFeedbackShiftRegister(int cap) {
        this(cap, 1e-10);
    }

    public LinearFeedbackShiftRegister() {
        this(0);
    }

    public double codeAt(int i) {
        return -cn.get(i);
    }

    private double estimateDelta() {
        summation.reset();
        int n = seq.size() - 1;
        for (int i = 0, until = cn.size(); i < until; i++) {
            summation.add(cn.get(i) * seq.get(n - i));
        }
        return summation.sum();
    }

    public void add(double x) {
        int n = seq.size();

        seq.add(x);
        double dn = estimateDelta();
        if (Math.abs(dn) < prec) {
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

        double factor = dn / dm;
        for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
            buf.set(i, buf.get(i) - factor * cm.get(i - (n - m)));
        }

        if (cn.size() < buf.size()) {
            DoubleArrayList tmp = cm;
            cm = cn;
            cn = tmp;
            m = n;
            dm = dn;
        }
        {
            DoubleArrayList tmp = cn;
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

    public Estimator newEstimator(double... init) {
        if (init.length != length()) {
            throw new IllegalArgumentException();
        }
        if (length() == 0) {
            return new ZeroEstimator();
        }
        return new EstimatorImpl(init);
    }

    public interface Estimator {
        double next();
    }

    private class ZeroEstimator implements Estimator {
        @Override
        public double next() {
            return 0;
        }
    }

    private class EstimatorImpl implements Estimator {
        double[] seq;
        int offset;
        int len;

        private EstimatorImpl(double[] seq) {
            this.seq = seq.clone();
            offset = 0;
            len = seq.length;
        }

        private double get(int i) {
            return seq[(i + offset) % len];
        }

        private void record(double x) {
            seq[offset++] = x;
            if (offset >= len) {
                offset = 0;
            }
        }

        public double next() {
            double ans = 0;
            for (int i = 1, until = cn.size(); i < until; i++) {
                ans += cn.get(i) * get(len - i);
            }
            ans = -ans;
            record(ans);
            return ans;
        }
    }
}
