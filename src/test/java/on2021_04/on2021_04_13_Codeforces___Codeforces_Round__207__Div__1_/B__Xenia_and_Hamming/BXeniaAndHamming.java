package on2021_04.on2021_04_13_Codeforces___Codeforces_Round__207__Div__1_.B__Xenia_and_Hamming;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

import java.util.Arrays;

public class BXeniaAndHamming {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long m = in.rl();
        char[] a = new char[(int) 1e6];
        char[] b = new char[(int) 1e6];
        int x = in.rs(a);
        int y = in.rs(b);
        int g = GCDs.gcd(x, y);
        long match = 0;
        int charset = 'z' - 'a' + 1;
        int[] cnts = new int[128];
        for (int i = 0; i < g; i++) {
            for (int j = i; j < x; j += g) {
                cnts[a[j]]++;
            }
            for (int j = i; j < y; j += g) {
                match += cnts[b[j]];
            }
            for (int j = i; j < x; j += g) {
                cnts[a[j]]--;
            }
        }
        long lcm = (long) x * y / g;
        long total = n * x;
        match *= total / lcm;
        long ans = total - match;
        out.println(ans);
    }
}
