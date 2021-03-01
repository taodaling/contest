package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.Arrays;

public class ERobotArm {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n,
                SumImpl::new, UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.pt[0] = i;
            return ans;
        });
        UpdateImpl upd = new UpdateImpl();
        SumImpl left = new SumImpl();
        SumImpl right = new SumImpl();
        for (int i = 0; i < m; i++) {
            debug.debug("st", st);
            int t = in.ri();
            int seg = in.ri();
            left.clear();
            right.clear();
            st.query(seg - 1, seg - 1, 0, n, left);
            st.query(seg, seg, 0, n, right);
            if (t == 1) {
                int len = in.ri();
                upd.clear();
                Complex.subtract(right.pt, left.pt, upd.plus);
                Complex.norm(upd.plus, upd.plus);
                Complex.shrink(upd.plus, len, upd.plus);
                st.update(seg, n, 0, n, upd);
            } else {
                double angle = -in.ri() / 180d * Math.PI;
                upd.clear();
                Complex.reverse(left.pt, upd.plus);
                st.update(seg, n, 0, n, upd);
                upd.clear();
                Complex.rotate(angle, upd.mul);
                st.update(seg, n, 0, n, upd);
                upd.clear();
                Complex.copy(left.pt, upd.plus);
                st.update(seg, n, 0, n, upd);
            }
            left.clear();
            st.query(n, n, 0, n, left);
            out.append(left.pt[0]).append(' ').append(left.pt[1]).println();
        }
    }
}

class Complex {
    public static void plus(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] + b[0];
        ans[1] = a[1] + b[1];
    }

    public static void subtract(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] - b[0];
        ans[1] = a[1] - b[1];
    }

    public static void mul(double[] a, double[] b, double[] ans) {
        double x1 = a[0];
        double y1 = a[1];
        double x2 = b[0];
        double y2 = b[1];
        ans[0] = x1 * x2 - y1 * y2;
        ans[1] = x1 * y2 + y1 * x2;
    }

    public static void divide(double[] a, double[] b, double[] ans) {
        conj(b, ans);
        mul(a, ans, ans);
        shrink(ans, 1 / length2(b), ans);
    }

    public static double length2(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }

    public static void reverse(double[] a, double[] ans) {
        ans[0] = -a[0];
        ans[1] = -a[1];
    }

    public static double length(double[] x) {
        return Math.sqrt(length2(x));
    }

    public static void norm(double[] x, double[] ans) {
        double sqrt = length(x);
        ans[0] = x[0] / sqrt;
        ans[1] = x[1] / sqrt;
    }

    public static void conj(double[] a, double[] ans) {
        ans[0] = a[0];
        ans[1] = -a[1];
    }

    public static void shrink(double[] a, double x, double[] ans) {
        ans[0] = a[0] * x;
        ans[1] = a[1] * x;
    }

    public static void rotate(double theta, double[] ans) {
        ans[0] = Math.cos(theta);
        ans[1] = Math.sin(theta);
    }

    public static void move(double x, double y, double[] ans) {
        ans[0] = x;
        ans[1] = y;
    }

    public static void copy(double[] a, double[] ans) {
        ans[0] = a[0];
        ans[1] = a[1];
    }
}

class UpdateImpl implements Update<UpdateImpl>, Cloneable {
    double[] mul = new double[2];
    double[] plus = new double[2];
    static double[] tmp = new double[2];

    @Override
    public void update(UpdateImpl update) {
        Complex.divide(update.plus, mul, tmp);
        Complex.plus(plus, tmp, plus);
        Complex.mul(mul, update.mul, mul);
    }

    @Override
    public void clear() {
        mul[0] = 1;
        mul[1] = 0;
        plus[0] = 0;
        plus[1] = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(mul[0] == 1 && mul[1] == 0 && plus[0] == 0 && plus[1] == 0);
    }

    @Override
    public UpdateImpl clone() {
        try {
            UpdateImpl ans = (UpdateImpl) super.clone();
            ans.plus = ans.plus.clone();
            ans.mul = ans.mul.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    double[] pt = new double[2];

    void clear() {
        pt[0] = 0;
        pt[1] = 0;
    }

    @Override
    public void add(SumImpl sum) {
        pt[0] += sum.pt[0];
        pt[1] += sum.pt[1];
    }

    @Override
    public void update(UpdateImpl update) {
        Complex.plus(pt, update.plus, pt);
        Complex.mul(pt, update.mul, pt);
    }

    @Override
    public void copy(SumImpl sum) {
        pt[0] = sum.pt[0];
        pt[1] = sum.pt[1];
    }

    @Override
    public SumImpl clone() {
        SumImpl s = new SumImpl();
        s.copy(this);
        return s;
    }

    @Override
    public String toString() {
        return Arrays.toString(pt);
    }
}