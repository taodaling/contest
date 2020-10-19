package contest;

import template.io.FastInput;
import template.math.LinearFunction;
import template.math.LongLinearFunction;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;

public class BBoxes {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);
        long[] b = new long[n];
        long[] c = new long[n];
        for (int i = 0; i < n; i++) {
            b[i] = a[i] - a[(i + 1) % n];
        }
        for (int i = 0; i < n; i++) {
            c[i] = b[i] - b[(i + n - 1) % n];
            if (c[i] % n != 0) {
                out.print("NO");
                return;
            }
        }
        LongLinearFunction[] funcs = new LongLinearFunction[n];
        funcs[0] = new LongLinearFunction(1, 0);
        long[] x = new long[n];
        for (int i = 1; i < n; i++) {
            //n (a[i] - a[i - 1]) = c[i]
            //a[i] = c[i] / n + a[i - 1];
            funcs[i] = funcs[i - 1].plus(new LongLinearFunction(c[i] / n));
        }
        long now = Arrays.stream(funcs).mapToLong(y -> y.b).sum();
        long sum = Arrays.stream(a).sum();
        long per = (long) (1 + n) * n / 2;
        if (sum % per != 0 || (sum / per - now) % n != 0) {
            out.print("NO");
            return;
        }

        x[0] = (sum / per - now) / n;
        for (int i = 1; i < n; i++) {
            x[i] = funcs[i].b + funcs[i].a * x[0];
        }
        long min = Arrays.stream(x).min().orElse(-1);
        if (min < 0) {
            out.print("NO");
            return;
        }
        long total = n * x[0];
        for (int i = 1; i < n; i++) {
            total += (n - i) * x[i];
        }
        if (total != a[0]) {
            out.print("NO");
            return;
        }
        out.print("YES");
        debug.run(() -> {
            debug.debug("x", Arrays.toString(x));
        });
    }
}
