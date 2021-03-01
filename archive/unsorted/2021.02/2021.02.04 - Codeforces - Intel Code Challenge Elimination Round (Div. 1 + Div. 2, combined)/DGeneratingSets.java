package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashSet;
import template.primitve.generated.datastructure.IntegerIterator;

import java.util.function.IntPredicate;

public class DGeneratingSets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] y = new int[n];
        in.populate(y);
        IntegerHashSet set = new IntegerHashSet(n * 2, false);
        IntPredicate p = m -> {
            set.clear();
            for (int x : y) {
                while (x != 0 && (x > m || set.contain(x))) {
                    x >>>= 1;
                }
                if (x > 0) {
                    set.add(x);
                }else{
                    return false;
                }
            }
            return true;
        };
        int ans = BinarySearch.firstTrue(p, 0, (int) 1e9);
        p.test(ans);
        for (IntegerIterator iterator = set.iterator(); iterator.hasNext(); ) {
            out.append(iterator.next()).append(' ');
        }
    }


}
