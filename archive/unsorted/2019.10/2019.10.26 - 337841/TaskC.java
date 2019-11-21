package contest;

import template.FastInput;
import template.FastOutput;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TaskC {
    int[] f;
    int[] suffix;
    int n;
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        f = new int[n + 1];
        suffix = new int[n + 1];
        Arrays.fill(f, -1);
        Arrays.fill(suffix, -1);
        f[n] = n;
        suffix[n] = n;

        int ans = f(1);
        out.println(ans);
    }

    public int f(int i) {
        if (i > n) {
            return 0;
        }
        if (f[i] == -1) {
            f[i] = f(i + 1);
            if (i + 3 <= n) {
                int since = i + 3;
                int until = n - i - 1;
                f[i] = mod.plus(f[i], suffix(since));
                f[i] = mod.plus(f[i], n - until);
                f[i] = mod.plus(f[i], mod.mul(n - 1, n - 1));
            } else {
                f[i] = mod.plus(f[i], mod.mul(n - 1, n));
            }
        }
        return f[i];
    }

    public int suffix(int i) {
        if (i > n) {
            return 0;
        }
        if (suffix[i] == -1) {
            suffix[i] = mod.plus(f[i], suffix(i + 1));
        }
        return suffix[i];
    }
}
