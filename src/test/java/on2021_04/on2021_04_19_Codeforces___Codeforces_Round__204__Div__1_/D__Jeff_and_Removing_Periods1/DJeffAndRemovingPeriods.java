package on2021_04.on2021_04_19_Codeforces___Codeforces_Round__204__Div__1_.D__Jeff_and_Removing_Periods1;




import template.datastructure.Range2DequeAdapter;
import template.datastructure.WaveletTrees;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBITExt;
import template.problem.RectCoverProblem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DJeffAndRemovingPeriods {
    int N = (int) 1e5;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int[] b = in.ri(m);

        Map<Integer, List<Integer>> group = IntStream.range(0, m).boxed().collect(Collectors.groupingBy(i -> b[i]));
        int[] reg = new int[N + 1];
        Arrays.fill(reg, -1);
        long[] prev = new long[m];
        for (int i = 0; i < m; i++) {
            prev[i] = reg[b[i]];
            reg[b[i]] = i;
        }
        List<RectCoverProblem.Rect> rects = new ArrayList<>(2 * m);
        for (List<Integer> list : group.values()) {
            int[] indices = list.stream().mapToInt(Integer::intValue).toArray();
            for (int i = 0; i < indices.length; i++) {
                int first = i == 0 ? -1 : indices[i - 1];
                if (i == indices.length - 1) {
                    rects.add(new RectCoverProblem.Rect(first + 1, indices[i], indices[i], m - 1, 1));
                    continue;
                }
                int l = i;
                int r = i + 1;
                int d = indices[r] - indices[l];
                while (r + 1 < indices.length && indices[r + 1] - indices[r] == d) {
                    r++;
                }
                i = r - 1;
                int last = r == indices.length - 1 ? m : indices[r + 1];
                for (int j = l; j <= r; j++) {
                    rects.add(new RectCoverProblem.Rect(first + 1, indices[j], indices[j], last - 1, 1));
                }
            }
        }


        WaveletTrees wt = new WaveletTrees(prev);
        int q = in.ri();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            int ans = wt.range(l, r, -1, l - 1);
            qs[i].x = l;
            qs[i].y = r;
            qs[i].ans = ans;
        }

        long[] res = RectCoverProblem.solve(rects.toArray(new RectCoverProblem.Rect[0]), qs);
        for(int i = 0; i < q; i++){
            if(res[i] == 0){
                qs[i].ans++;
            }
        }

        for (Query query : qs) {
            out.println(query.ans);
        }
    }


}

class Query extends RectCoverProblem.Point {
    int ans;
}