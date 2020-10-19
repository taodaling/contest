package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class AGottaCatchEmAll {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[(int) 1e5];
        int n = in.readString(s, 0);
        int[] cnts = new int[128];
        for (int i = 0; i < n; i++) {
            cnts[s[i]]++;
        }
        int min = (int) 1e8;
        min = Math.min(min, cnts['B']);
        min = Math.min(min, cnts['u'] / 2);
        min = Math.min(min, cnts['l']);
        min = Math.min(min, cnts['b']);
        min = Math.min(min, cnts['a'] / 2);
        min = Math.min(min, cnts['s']);
        min = Math.min(min, cnts['r']);
        out.println(min);
    }
}
