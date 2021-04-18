package on2021_04.on2021_04_12_Codeforces___Codeforces_Round__210__Div__1_.D__Levko_and_Sets;



import template.algo.DivisionDescending;
import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.util.HashSet;
import java.util.Set;

public class DLevkoAndSets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int p = in.ri();
        Set<Integer> set = new HashSet<>(n);
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            set.add(x);
        }
        int phi = p - 1;
        LongFactorization factorization = new LongFactorization(phi);
        UnorderedDivisorVisitor visitor = new UnorderedDivisorVisitor();
        visitor.init(factorization);
        DivisorCollection dc = new DivisorCollection(visitor, true);
        int gcd = phi;
        for (int i = 0; i < m; i++) {
            gcd = GCDs.gcd(gcd, in.ri());
        }
        gcd %= phi;
        if (gcd == 0) {
            out.println(1);
            return;
        }

        Power power = new Power(p);
        MultipleSetMaintainer maintainer = new MultipleSetMaintainer(dc);

        for (int x : set) {
            long log = phi / DivisionDescending.find(factorization, y -> power.pow(x, y) == 1);
            long step = GCDs.gcd(gcd * log, phi);
            maintainer.add(step);
        }

        long ans = maintainer.size();
        out.println(ans);
    }
}
