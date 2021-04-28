package on2021_04.on2021_04_26_Codeforces___Codeforces_Round__198__Div__1_.C__Iahub_and_Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;

public class CIahubAndPermutations {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e4, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] P = new int[n];
        boolean[] occur = new boolean[n];
        for (int i = 0; i < n; i++) {
            P[i] = in.ri() - 1;
            if(P[i] >= 0){
                occur[P[i]] = true;
            }
        }
        int a = 0;
        for (int x : P) {
            if (x >= 0) {
                a++;
            }
        }
        int b = n - a;
        int b1 = 0;
        for(int i = 0; i < n; i++){
            if(P[i] < 0 && !occur[i]){
                b1++;
            }
        }
        long ans = 0;
        for (int x = 0; x <= b1; x++) {
            long contrib = 1;
            contrib = contrib * comb.combination(b1, x) % mod
                    * comb.combination(n - a - x, b - x) % mod
                    * fact.fact(b - x) % mod
                    * fact.fact(n - a - b) % mod;
            if(x % 2 == 1){
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        ans = ans * fact.fact(n - a - b) % mod;
        out.println(ans);
    }
}
