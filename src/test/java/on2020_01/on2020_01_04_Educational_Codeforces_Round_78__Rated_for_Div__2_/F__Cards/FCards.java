package on2020_01.on2020_01_04_Educational_Codeforces_Round_78__Rated_for_Div__2_.F__Cards;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class FCards {
    Modular mod = new Modular(998244353);
    Composite comp = new Composite(5000, mod);
    Power power = new Power(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        CachedPow pm = new CachedPow(m, mod);
        int[] xk = new int[k + 1];
        for(int i = 0; i <= k; i++){
            xk[i] = power.pow(i, k);
        }

        int ans = 0;
        BinomialComposite bc = new BinomialComposite(n, k, mod, true);
        for(int t = 0; t <= k; t++){
            int p1 = bc.get(t);
            int p2 = 0;
            for(int i = 0; i <= t; i++){
                int contrib = comp.composite(t, i);
                contrib = mod.mul(contrib, xk[t - i]);
                if(i % 2 == 1){
                    contrib = mod.valueOf(-contrib);
                }
                p2 = mod.plus(p2, contrib);
            }
            int p3 = pm.inverse(t);

            int contrib = mod.mul(p1, p2);
            contrib = mod.mul(contrib, p3);
            ans = mod.plus(ans, contrib);
        }

        out.println(ans);
    }

}
