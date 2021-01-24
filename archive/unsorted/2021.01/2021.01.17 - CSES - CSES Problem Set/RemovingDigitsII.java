package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;

public class RemovingDigitsII {
    long[][][][][] nine = new long[19][10][10][10][];
    long[][][][][] zero = new long[19][10][10][10][];
    long[][][] bf = new long[100][10][];

    public long[] bf(int n, int a, int b, int h) {
        int v = a * 10 + b;
        if (n == 0) {
            v = b;
        }
        if (bf[v][h] == null) {
            int remain = v;
            int step = 0;
            while (remain > 0) {
                int sub = Math.max(remain / 10, remain % 10);
                sub = Math.max(sub, h);
                if (sub > remain) {
                    break;
                }
                step++;
                remain -= sub;
            }
            bf[v][h] = new long[]{step, remain};
        }
        return bf[v][h];
    }

    public long[] nine(int n, int a, int b, int h) {
        if (n <= 1) {
            return bf(n, a, b, h);
        }
        if (a == 0) {
            return nine(n - 1, 9, b, h);
        }
        if (nine[n][a][b][h] == null) {
            long[] tail = nine(n - 1, 9, b, Math.max(h, a));
            long[] res = zero(n, a, (int) tail[1], h);
            long[] ans = new long[]{tail[0] + res[0], res[1]};
            nine[n][a][b][h] = ans;
        }
        return nine[n][a][b][h];
    }

    public long max(int a, int b, int c) {
        int ans = Math.max(a, b);
        if (ans < c) {
            ans = c;
        }
        return ans;
    }

    public long[] zero(int n, int a, int b, int h) {
        if (n <= 1) {
            return bf(n, a, b, h);
        }
        if (a == 0) {
            return bf(0, 0, b, h);
        }
        if (zero[n][a][b][h] == null) {
            long[] tail = bf(0, 0, b, Math.max(a, h));
            long[] res = nine(n, a - 1, (int) (tail[1] + 10 - max((int) tail[1], a, h)), h);
            long[] ans = new long[]{tail[0] + res[0] + 1, res[1]};
            zero[n][a][b][h] = ans;
        }
        return zero[n][a][b][h];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        char[] s = Long.toString(n).toCharArray();
        if (s.length == 1) {
            out.println(1);
            return;
        }
        for (int i = 0; i < s.length; i++) {
            s[i] -= '0';
        }
        int[] max = new int[s.length];
        for (int i = 1; i < s.length; i++) {
            max[i] = Math.max(max[i - 1], s[i - 1]);
        }
        long sum = 0;
        int remain = s[s.length - 1];
        for (int i = s.length - 2; i >= 0; i--) {
            int len = s.length - 1 - i;
            long[] res = zero(len, s[i], remain, max[i]);
            sum += res[0];
            remain = (int) res[1];
        }
        if (remain > 0) {
            sum++;
            remain = 0;
        }
        out.println(sum);
    }
}
