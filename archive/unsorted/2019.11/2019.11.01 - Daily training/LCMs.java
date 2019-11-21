package contest;

import template.FastInput;
import template.FastOutput;

public class LCMs {
    Modular mod = new Modular(998244353);
    Power pow = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(1000000, mod);
    Factorial fact = new Factorial(1000000, mod);
    Composite comp = new Composite(fact);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = 1000000;

        int n = in.readInt();
        int[] cnts = new int[limit + 1];
        int[] sum = new int[limit + 1];
        int[] pairSum = new int[limit + 1];
        int[] selfContri = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }
        for(int i = 1; i <= limit; i++){
            selfContri[i] = mod.mul(comp.composite(cnts[i], 2), mod.mul(i, i));
        }
        for(int i = 1; i <= limit; i++){
            pairSum[i] = selfContri[i];
            sum[i] = mod.mul(i, cnts[i]);
            for(int j = i + i; j <= limit; j += i){
                pairSum[i] = mod.plus(pairSum[i], selfContri[j]);
                pairSum[i] = mod.plus(pairSum[i], mod.mul(sum[i], mod.mul(cnts[j], j)));
                sum[i] = mod.plus(sum[i], mod.mul(cnts[j], j));
            }
        }


        int ans = 0;
        for (int i = limit; i >= 1; i--) {
            for (int j = i + i; j <= limit; j += i) {
                pairSum[i] = mod.subtract(pairSum[i], pairSum[j]);
            }
            ans = mod.plus(ans, mod.mul(pairSum[i], inverseNumber.inverse(i)));
        }

        out.println(ans);
    }
}
