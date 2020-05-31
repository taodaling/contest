package contest;

import template.math.Modular;
import template.math.Power;
import template.math.RelativePrimeModLog;
import template.utils.Debug;

public class WrongBase {
    //Debug debug = new Debug(true);
    public int getSum(int g, int h, int a, int d, int n) {
        //g^x = h
        Modular mod = new Modular(998244353);
        RelativePrimeModLog log = new RelativePrimeModLog(g, mod);
        Power pow = new Power(mod);
        if (h == 0) {
            return 0;
        }
        int x = log.log(h);
      //  debug.debug("x", x);
        if(pow.pow(g, x) != h){
            throw new RuntimeException();
        }
        int val = a;
        int ans = 0;
        for (int i = 0; i < n; i++) {
          //  debug.debug("val", val);
            ans = mod.plus(ans, pow.pow(val, x));
            val = mod.plus(val, d);
        }

        return ans;
    }
}
