package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;
import template.PermutationUtils;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }

        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(p, n);
        int[] a = new int[n];
        int[] b = new int[n];
        int[] sum = new int[n];
        for (int i = 0; i < n; i++) {
            sum[i] = pp.apply(i, -1);
        }

        a[0] = 0;
        b[0] = sum[0];
        for (int i = 1; i < n; i++) {
            a[i] = a[i - 1] + 1;
            b[i] = sum[i] - a[i];
            if (b[i] >= b[i - 1]) {
                int d = b[i] - (b[i - 1] - 1);
                b[i] = b[i - 1] - 1;
                a[i] += d;
            }
        }

        int minA = a[ArrayUtils.indexOfMin(a, 0, n)];
        int minB = b[ArrayUtils.indexOfMin(b, 0, n)];
        for (int i = 0; i < n; i++) {
            a[i] -= minA - 1;
            b[i] -= minB - 1;
        }

        for (int i = 0; i < n; i++) {
            out.append(a[i]).append(' ');
        }
        out.println();
        for (int i = 0; i < n; i++) {
            out.append(b[i]).append(' ');
        }
    }

    public int valueOf(int one, int zero) {
        return one * 30000 + zero;
    }
}
