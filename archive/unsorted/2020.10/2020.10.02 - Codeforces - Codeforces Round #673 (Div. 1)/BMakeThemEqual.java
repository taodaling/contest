package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BMakeThemEqual {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        debug.debug("testNumber", testNumber);
        op.clear();

        int n = in.readInt();
        a = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        int sum = Arrays.stream(a).sum();
        if (sum % n != 0) {
            out.println(-1);
            return;
        }

        avg = sum / n;
        for (int i = n; i >= 2; i--) {
            int req = (i - a[i] % i) % i;
            if (req > a[1]) {
                if (a[i] < i) {
                    continue;
                }
                add(i, 1, 1);
            }
            add(1, i, req);
            add(i, 1, a[i] / i);
        }

        while (true) {
            boolean process = false;
            for (int i = 2; i <= n; i++) {
                if (a[i] == 0) {
                    continue;
                }
                int req = (i - a[i] % i) % i;
                if (req <= a[1]) {
                    add(1, i, req);
                    add(i, 1, a[i] / i);
                    process = true;
                }
            }
            if (!process) {
                break;
            }
        }

        for (int i = 2; i <= n; i++) {
            if (a[i] > avg) {
                out.println(-1);
                return;
            }
        }

        for (int i = 2; i <= n; i++) {
            add(1, i, avg - a[i]);
        }

        assert equal(a);
        assert op.size() <= 3 * n;
        out.println(op.size());
        for (int[] seq : op) {
            for(int x : seq){
                out.print(x);
                out.append(' ');
            }
            out.println();
        }

    }

    public boolean equal(int[] a) {
        for (int i = 2; i < a.length; i++) {
            if (a[i] != a[i - 1]) {
                return false;
            }
        }
        return true;
    }

    List<int[]> op = new ArrayList<>();
    int[] a;
    int avg;

    public void add(int i, int j, int x) {
        if (x == 0) {
            return;
        }
        a[i] -= x * i;
        a[j] += x * i;
        op.add(new int[]{i, j, x});
    }
}
