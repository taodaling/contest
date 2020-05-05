package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.io.*;
import java.util.StringTokenizer;

public class DSomethingWithXORQueries {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.readInt();

        p0b = new int[n];
        pb0 = new int[n];
        for (int i = 0; i < n; i++) {
            p0b[i] = ask(0, i);
            pb0[i] = ask(i, 0);
        }

        int[] p = new int[n];
        int[] b = new int[n];

        int[] ansP = null;
        int possible = 0;
        for (int i = 0; i < n; i++) {
            p[0] = i;
            for (int j = 1; j < n; j++) {
                p[j] = xorPP(0, j) ^ p[0];
            }
            for (int j = 0; j < n; j++) {
                b[j] = xorPB(0, j) ^ p[0];
            }

            boolean valid = true;
            for (int j = 0; j < n; j++) {
                if (p[j] >= n || b[p[j]] != j) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                continue;
            }
            debug.debug("i", i);
            possible++;
            if (ansP == null) {
                ansP = p.clone();
            }
        }

        out.println("!");
        out.println(possible);
        out.flush();
        if (ansP == null) {
            return;
        }
        for (int x : ansP) {
            out.append(x);
            out.append(' ');
        }
        out.flush();
    }

    int[] p0b;
    int[] pb0;

    public int xorPB(int i, int j) {
        return p0b[j] ^ pb0[i] ^ p0b[0];
    }

    public int xorPP(int i, int j) {
        return pb0[i] ^ pb0[j];
    }

    FastInput in;
    FastOutput out;

    public int ask(int a, int b) {
        out.printf("? %d %d\n", a, b);
        out.flush();
        return in.readInt();
    }

}
