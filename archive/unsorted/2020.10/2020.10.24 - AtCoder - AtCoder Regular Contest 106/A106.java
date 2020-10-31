package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class A106 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long n = in.readLong();
        for (long i = 1, a = 0; i < n; i = i * 3, a++) {
            long diff = n - i;
            int log = 0;
            while (diff % 5 == 0) {
                diff /= 5;
                log++;
            }
            if (diff == 1 && a > 0 && log > 0) {
                out.print(a);
                out.append(' ');
                out.println(log);
                return;
            }
        }
        out.println(-1);
    }
}
