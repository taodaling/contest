package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntegerList;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        IntegerList factors = new IntegerList();
        for (int i = 1; i * i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            factors.add(i);
            if (n / i != i) {
                factors.add(n / i);
            }
        }

        factors.sort();
        IntegerList cnt = new IntegerList();
        cnt.expandWith(0, factors.size());

        int ans = 0;
        for (int i = 0; i < factors.size(); i++) {
            int j = factors.get(i);
            int ways = pow.pow(k, DigitUtils.ceilDiv(j, 2));

            for(int t = 0; t < i; t++){
                if(j % factors.get(t) == 0){
                    ways = mod.subtract(ways, cnt.get(t));
                }
            }

            cnt.set(i, ways);


            if (j % 2 == 0) {
                ways = mod.mul(ways, j / 2);
            } else {
                ways = mod.mul(ways, j);
            }
            ans = mod.plus(ways, ans);
        }

        out.println(ans);
    }
}
