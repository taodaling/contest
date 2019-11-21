package template.math;

import template.datastructure.DoubleList;

public class DoubleLinearFeedbackShiftRegister {
    private DoubleList cm;
    int m = -1;
    double dm;
    private DoubleList cn;
    private DoubleList buf;
    private DoubleList seq;

    public DoubleLinearFeedbackShiftRegister(int cap) {
        cm = new DoubleList(cap + 1);
        cn = new DoubleList(cap + 1);
        seq = new DoubleList(cap + 1);
        buf = new DoubleList(cap + 1);

        cn.add(1);
    }

    public DoubleLinearFeedbackShiftRegister() {
        this(0);
    }

    public double codeAt(int i){
        return cn.get(i);
    }

    private double estimateDelta() {
        int n = seq.size() - 1;
        double ans = 0;
        for (int i = 0, until = cn.size(); i < until; i++) {
            ans += cn.get(i) * seq.get(n - i);
        }
        return ans;
    }

    public void add(double x) {
        int n = seq.size();

        seq.add(x);
        double dn = estimateDelta();
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

        double factor = dn / dm;
        for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
            buf.set(i, buf.get(i) - factor * cm.get(i - (n - m)));
        }

        if (cn.size() < buf.size()) {
            DoubleList tmp = cm;
            cm = cn;
            cn = tmp;
            m = n;
            dm = dn;
        }
        {
            DoubleList tmp = cn;
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
