package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class WeirdAlgorithm {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long n = in.readInt();
        out.println(n);
        while (n != 1) {
            if (n % 2 == 0) {
                n /= 2;
            }else{
                n = n * 3 + 1;
            }
            out.println(n);
        }
    }
}
