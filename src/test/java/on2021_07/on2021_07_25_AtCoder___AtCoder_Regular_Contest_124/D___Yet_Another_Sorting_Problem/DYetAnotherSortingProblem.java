package on2021_07.on2021_07_25_AtCoder___AtCoder_Regular_Contest_124.D___Yet_Another_Sorting_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class DYetAnotherSortingProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] p = in.ri(n + m);
        for (int i = 0; i < p.length; i++) {
            p[i]--;
        }
        Permutation perm = new Permutation(p);
        List<IntegerArrayList> circles = perm.extractCircles(2);
        int[] cnt = new int[2];
        int ans = 0;
        for (IntegerArrayList c : circles) {
            int type = 0;
            for (int i = 0; i < c.size(); i++) {
                int x = c.get(i);
                if (x < n) {
                    type |= 1;
                } else {
                    type |= 2;
                }
            }
            if (type == 1) {
                cnt[0]++;
            }
            if (type == 2) {
                cnt[1]++;
            }
            ans += c.size() - 1;
        }
        int max = Math.max(cnt[0], cnt[1]);
        ans += max * 2;
        out.println(ans);
    }
}
