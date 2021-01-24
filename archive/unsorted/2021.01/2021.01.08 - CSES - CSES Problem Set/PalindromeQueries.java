package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.ModifiableIntRangeHash;

public class PalindromeQueries {
    int n;

    public int mirror(int i) {
        return n - 1 - i;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        HashData[] hds = HashData.doubleHashData(n);
        ModifiableIntRangeHash order = new ModifiableIntRangeHash(hds[0], hds[1], n);
        ModifiableIntRangeHash reverse = new ModifiableIntRangeHash(hds[0], hds[1], n);
        for (int i = 0; i < n; i++) {
            char c = in.rc();
            order.set(i, c);
            reverse.set(mirror(i), c);
        }
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            if (t == 1) {
                int k = in.ri() - 1;
                int x = in.rc();
                order.set(k, x);
                reverse.set(mirror(k), x);
            }else{
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                if(order.hash(a, b) == reverse.hash(mirror(b), mirror(a))){
                    out.println("YES");
                }else{
                    out.println("NO");
                }
            }
        }
    }
}
