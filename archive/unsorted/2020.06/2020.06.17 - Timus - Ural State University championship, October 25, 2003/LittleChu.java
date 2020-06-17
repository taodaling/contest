package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.Modular;
import template.math.PrimitiveRoot;

public class LittleChu {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

//        EulerSieve es = new EulerSieve(65536);
//        for(int i = 0; i < es.getPrimeCount(); i++) {
//            n = es.get(i);
            Modular mod = new Modular(n);
            PrimitiveRoot pr = new PrimitiveRoot(mod);
            int ans = pr.findMaxPrimitiveRoot();
            out.println(ans);
//        }
    }
}
