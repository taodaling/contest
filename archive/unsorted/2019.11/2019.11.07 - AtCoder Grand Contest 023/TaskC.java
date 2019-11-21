package contest;

import template.FastInput;
import template.FastOutput;

public class TaskC {

    Modular mod = new Modular(1e9 + 7);
    Factorial fact = new Factorial(1000000, mod);
    Composite comp = new Composite(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();


        int[] t = new int[n];
        for(int i = 0; i < n; i++){
            int minus = cover(n, i);
            minus = mod.mul(fact.fact(n - 1 - i), minus);
            minus = mod.mul(fact.fact(i), minus);
            t[i] = mod.subtract(fact.fact(n - 1), minus);
        }

        int ans = 0;
        for(int i = 0; i < n; i++){
            ans = mod.plus(ans, t[i]);
        }

        out.println(ans);
    }

    public int cover(int n, int k){
        int b = n - 1 - k;
        int a = k - b;
        if(a < 1 || b < 0){
            return 0;
        }
        int ans = fact.fact(k - 1);
        ans = mod.mul(ans, fact.invFact(a - 1));
        ans = mod.mul(ans, fact.invFact(b));
        return ans;
    }
}
