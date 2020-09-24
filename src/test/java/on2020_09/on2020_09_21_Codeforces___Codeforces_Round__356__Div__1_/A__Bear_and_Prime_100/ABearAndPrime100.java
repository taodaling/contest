package on2020_09.on2020_09_21_Codeforces___Codeforces_Round__356__Div__1_.A__Bear_and_Prime_100;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

import java.util.ArrayList;
import java.util.List;

public class ABearAndPrime100 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        EulerSieve sieve = new EulerSieve(100);
        List<Integer> factors = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int x = sieve.get(i);
            if (x > 50) {
                continue;
            }
            out.println(x).flush();
            String ans = in.readString();
            if (ans.equals("yes")) {
                factors.add(x);
            }
        }

        String prime = "prime";
        String composite = "composite";
        if (factors.size() == 0) {
            out.println(prime).flush();
            return;
        }
        if (factors.size() == 1) {
            int x = factors.get(0);
            if (x * x > 100) {
                out.println(prime).flush();
                return;
            }
            if (x * x <= 100) {
                out.println(x * x).flush();
                if (in.readString().equals("no")) {
                    out.println(prime).flush();
                    return;
                }
            }
        }
        out.println(composite).flush();
    }
}
