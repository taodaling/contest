package template.primitve.generated.datastructure;

import java.util.Arrays;

public class DoubleBITExt {
    DoubleBIT delta;
    DoubleBIT idelta;

    public DoubleBITExt(int n) {
        delta = new DoubleBIT(n + 1);
        idelta = new DoubleBIT(n + 1);
    }

    public void clear(int n) {
        delta.clear(n);
        idelta.clear(n);
    }

    public void clear() {
        delta.clear();
        idelta.clear();
    }

    public void clear(IntToDoubleFunction func, int n) {
        delta.clear(i -> {
            double ans = func.apply(i);
            if (i > 1) {
                ans -= func.apply(i - 1);
            }
            return ans;
        }, n);
        idelta.clear(i -> {
            double ans = func.apply(i);
            if (i > 1) {
                ans -= func.apply(i - 1);
            }
            return ans * i;
        }, n);
    }

    public void update(int l, int r, double x) {
        delta.update(l, x);
        delta.update(r + 1, -x);
        idelta.update(l, l * x);
        idelta.update(r + 1, (r + 1) * -x);
    }

    public double query(int i) {
        if (i <= 0) {
            return 0;
        }
        double x = delta.query(i);
        return x * (i + 1) - idelta.query(i);
    }

    public double query(int l, int r) {
        return query(r) - query(l - 1);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("[");
        for (int i = 1; i <= delta.size(); i++) {
            ans.append(query(i, i)).append(',');
        }
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append("]");
        return ans.toString();
    }
}
