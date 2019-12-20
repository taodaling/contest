package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class FCards {
    Modular mod = new Modular(998244353);
    Composite comp = new Composite(10000, mod);
    Power power = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(10000, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();


        int invM = power.inverse(m);
        CachedPow invMPower = new CachedPow(invM, mod);
        int[] powCache = new int[k + 1];
        for(int i = 0; i <= k; i++){
            powCache[i] = power.pow(i, k);
        }


        int ans = 0;
        for (int i = 1; i <= n && i <= k; i++) {
            int way1 = choose(n, i);
            int way2 = 0;
            for (int j = 0; j <= i; j++) {
                int local = mod.mul(comp.composite(i, j), powCache[i - j]);
                if(j % 2 == 1){
                    local = mod.valueOf(-local);
                }
                way2 = mod.plus(way2, local);
            }

            int prob = invMPower.pow(i);

            int contrib = mod.mul(prob, mod.mul(way1, way2));
            ans = mod.plus(ans, contrib);
        }

        out.println(ans);
    }

    public int choose(int n, int k) {
        if (n < k) {
            return 0;
        }
        if (k == 0) {
            return 1;
        }
        return mod.mul(choose(n - 1, k - 1), mod.mul(n, inverseNumber.inverse(k)));
    }
}
