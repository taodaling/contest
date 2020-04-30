package on2020_04.on2020_04_30_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.F__Function;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.PermutationUtils;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerList;

import java.util.List;

public class FFunction {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];

        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        for(int i = 0; i < m; i++){
            b[i] = in.readInt();
        }

        PermutationUtils.PowerPermutation pa = new PermutationUtils.PowerPermutation(a);
        PermutationUtils.PowerPermutation pb = new PermutationUtils.PowerPermutation(b);
        List<IntegerList> aC = pa.extractCircles();
        List<IntegerList> bC = pb.extractCircles();

        int limit = Math.max(n, m);
        int[] cnts = new int[limit + 1];
        for (IntegerList list : bC) {
            cnts[list.size()] += list.size();
        }
        for (int i = m; i >= 1; i--) {
            for (int j = i + i; j <= limit; j += i) {
                cnts[j] += cnts[i];
            }
        }

        int ans = 1;
        for (IntegerList list : aC) {
            int s = list.size();
            ans = mod.mul(cnts[s], ans);
        }

        out.printf("Case #%d: %d\n", testNumber, ans);
    }
}
