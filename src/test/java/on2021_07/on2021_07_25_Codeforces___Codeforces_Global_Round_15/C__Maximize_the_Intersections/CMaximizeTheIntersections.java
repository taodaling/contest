package on2021_07.on2021_07_25_Codeforces___Codeforces_Global_Round_15.C__Maximize_the_Intersections;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMaximizeTheIntersections {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] pairs = new int[k][2];
        int[] match = new int[2 * n];
        Arrays.fill(match, -1);
        List<int[]> pairList = new ArrayList<>(n);
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 2; j++) {
                pairs[i][j] = in.ri() - 1;
            }
            match[pairs[i][0]] = pairs[i][1];
            match[pairs[i][1]] = pairs[i][0];
            pairList.add(pairs[i]);
        }
        IntegerArrayList candList = new IntegerArrayList(n * 2);
        for (int i = 0; i < n * 2; i++) {
            if (match[i] == -1) {
                candList.add(i);
            }
        }

        int[] cand = candList.toArray();
        for (int i = 0; i < cand.length / 2; i++) {
            pairList.add(new int[]{cand[i], cand[i + cand.length / 2]});
        }
        for (int[] p : pairList) {
            if (p[0] > p[1]) {
                SequenceUtils.swap(p, 0, 1);
            }
            debug.debugArray("p", p);
        }


        int ans = 0;
        for (int i = 0; i < pairList.size(); i++) {
            for (int j = i + 1; j < pairList.size(); j++) {
                int[] a = pairList.get(i);
                int[] b = pairList.get(j);
                if (a[0] <= b[0] && b[1] <= a[1] || b[0] <= a[0] && a[1] <= b[1] || a[1] <= b[0] || b[1] <= a[0]) {

                }else{
                    debug.debugArray("a", a);
                    debug.debugArray("b", b);
                    ans++;
                }
            }
        }
        out.println(ans);
        debug.log("end");
    }
    Debug debug = new Debug(false);

}
