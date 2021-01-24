package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CollectingNumbersII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        x = new int[n];
        inv = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.ri() - 1;
            inv[x[i]] = i;
        }
        sum = 1;
        for (int i = 1; i < n; i++) {
            if (inv[i] < inv[i - 1]) {
                sum++;
            }
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (a != b) {
                int va = x[a];
                int vb = x[b];
                remove(a, va);
                remove(b, vb);
                set(a, vb);
                set(b, va);
            }
            out.println(sum);
        }
    }

    int n;
    int sum;
    int[] x;
    int[] inv;

    public void remove(int i, int v) {
        if (v > 0 && inv[v - 1] != -1 && inv[v] < inv[v - 1]) {
            sum--;
        }
        if (v + 1 < n && inv[v + 1] != -1 && inv[v + 1] < inv[v]) {
            sum--;
        }
        inv[v] = -1;
        x[i] = -1;
    }

    public void set(int i, int v) {
        inv[v] = i;
        x[i] = v;
        if (v > 0 && inv[v - 1] != -1 && inv[v] < inv[v - 1]) {
            sum++;
        }
        if (v + 1 < n && inv[v + 1] != -1 && inv[v + 1] < inv[v]) {
            sum++;
        }
    }
}
