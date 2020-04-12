package contest;

import template.math.Factorization;
import template.math.LinearFunctionCut;
import template.primitve.generated.datastructure.LongIterator;
import template.primitve.generated.datastructure.LongList;
import template.utils.Debug;

public class CardboardBoxes {
    Debug debug = new Debug(false);

    public long count(long S) {
        //2 (x + y)(x + z);
        if (S % 2 != 0) {
            return 0;
        }
        S /= 2;

        long ans = 0;
        LinearFunctionCut cut = new LinearFunctionCut();
        LongList factor = Factorization.factorizeNumber(S);
        for (LongIterator iterator = factor.iterator(); iterator.hasNext(); ) {
            long i = iterator.next();
            if (S % i != 0) {
                continue;
            }
            //x + y = i
            //x + z = S / i
            // long xy = i;
            // long xz = S / i;
            cut.reset();
            cut.greaterThan(1, 0);
            //i - x >= x => -2x + i >= 0
            cut.greaterThanOrEqual(-2, i, 0);
            cut.greaterThanOrEqual(-1, S / i, 1);
            ans += cut.length();

//            for(long j = cut.getL(); j <= cut.getR(); j++){
//                debug.debug("xyz", "x=" + j + ",y=" + (i - j) + ",z=" + (S / i - j));
//            }
        }
        return ans;
    }
}
