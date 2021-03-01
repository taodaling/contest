package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerIntervalMap;
import template.rand.HashData;
import template.rand.IntRangeHash;
import template.utils.Pair;

public class BTavasAndMalekas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[] s = new char[n];
        int len = in.rs(s);
        HashData[] hds = HashData.doubleHashData(len);
        IntRangeHash hash = new IntRangeHash(hds[0], hds[1], len);
        hash.populate(i -> s[i], len);
        int last = -len;
        IntegerIntervalMap map = new IntegerIntervalMap();
        for (int i = 0; i < m; i++) {
            int index = in.ri();
            if (last + len - 1 >= index) {
                int intersect = last + len - 1 - index + 1;
                if (hash.hash(0, intersect - 1) != hash.hash(len - intersect, len - 1)) {
                    out.println(0);
                    return;
                }
            }
            last = index;
            map.add(index, index + len);
        }
        int mod = (int) 1e9 + 7;
        Power pow = new Power(mod);
        int ans = pow.pow('z' - 'a' + 1, n - map.total());
        out.println(ans);
    }
}
