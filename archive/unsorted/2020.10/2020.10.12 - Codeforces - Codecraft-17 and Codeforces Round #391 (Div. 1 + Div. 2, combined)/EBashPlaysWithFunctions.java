package contest;

import template.io.FastInput;
import template.math.Combination;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class EBashPlaysWithFunctions {
    int mod = (int) (1e9 + 7);
    Combination comb = new Combination((int) 2e6, mod);

    IntegerMultiWayStack stack = Factorization.factorizeRangePrime((int) 1e6);

    public int size(IntegerIterator iterator) {
        int ans = 0;
        while (iterator.hasNext()) {
            ans++;
            iterator.next();
        }
        return ans;
    }


    IntegerArrayList buf = new IntegerArrayList(10);


    int r;
    int[] data;

    public long dfs(int i, long prod, int retain) {
        if (prod == 0) {
            return 0;
        }
        if (i == data.length) {
            return prod * (1 << retain) % mod;
        }
        int n = data[i];
        //retain
        //x1+x2+...+xr+y=n-1
        return dfs(i + 1, prod * comb.combination(n - 1 + r, r) % mod, retain + 1) +
                dfs(i + 1, prod * comb.combination(n + r - 1, n) % mod, retain);
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        r = in.readInt();
        int n = in.readInt();
        buf.clear();
        buf.addAll(stack.iterator(n));
        int[] cnts = buf.toArray();
        for (int i = 0; i < cnts.length; i++) {
            int x = 0;
            while (n % cnts[i] == 0) {
                n /= cnts[i];
                x++;
            }
            cnts[i] = x;
        }
        this.data = cnts;
        long ans = dfs(0, 1, 0) % mod;
        out.println(ans);
    }
}
