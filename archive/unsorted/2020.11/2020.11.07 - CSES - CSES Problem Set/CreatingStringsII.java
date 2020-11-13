package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;

import java.util.Arrays;

public class CreatingStringsII {
    int mod = (int)1e9 + 7;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] cnts = new int['z' - 'a' + 1];
        while (in.hasMore()) {
            cnts[in.readChar() - 'a']++;
        }
        int sum = Arrays.stream(cnts).sum();
        Factorial fact = new Factorial(sum, mod);
        long prod = fact.fact(sum);
        for(int x : cnts){
            prod = prod * fact.invFact(x) % mod;
        }
        out.println(prod);
    }
}
