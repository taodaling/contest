package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class CSolutions {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        if (m < 0) {
            out.println(-1);
            return;
        }
        int a;
        int b;
        if (m == 0) {
            a = n;
            b = 0;
        } else {
            b = m + 2;
            a = n - b;
        }
        if (a < 0) {
            out.println(-1);
            return;
        }
        int inf = (int) 1e9;
        for (int i = 1; i <= a; i++) {
            out.print(i * 2);
            out.append(' ');
            out.println(i * 2 + 1);
        }
        int offset = (int) 1e6;
        if (b > 0) {
            out.print(offset);
            out.append(' ');
            out.println(inf);
        }
        for (int i = 1; i < b; i++) {
            out.print(offset + i * 2);
            out.append(' ');
            out.println(offset + i * 2 + 1);
        }
    }
}
