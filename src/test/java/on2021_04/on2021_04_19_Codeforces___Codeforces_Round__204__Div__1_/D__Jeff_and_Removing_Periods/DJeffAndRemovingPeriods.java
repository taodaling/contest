package on2021_04.on2021_04_19_Codeforces___Codeforces_Round__204__Div__1_.D__Jeff_and_Removing_Periods;



import template.datastructure.ActiveSegment;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.WaveletTrees;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerBITExt;
import template.primitve.generated.datastructure.IntegerBinaryFunction;

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
        List<Rect> rects = new ArrayList<>(2 * m);
        for (List<Integer> list : group.values()) {
            int[] indices = list.stream().mapToInt(Integer::intValue).toArray();
            for (int i = 0; i < indices.length; i++) {
                int first = i == 0 ? -1 : indices[i - 1];
                if (i == indices.length - 1) {
                    rects.add(new Rect(first + 1, indices[i], indices[i], m - 1));
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
                    rects.add(new Rect(first + 1, indices[j], indices[j], last - 1));
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
            qs[i].l = l;
            qs[i].r = r;
            qs[i].ans = ans;
        }

//        Query[] sortedByR = qs.clone();
//        Arrays.sort(sortedByR, Comparator.comparingInt(x -> x.r));
//        int offset = 2;
//        IntegerBIT bit = new IntegerBIT(N + offset);
//        for (int i = 0, head = 0; i < m; i++) {
//            bit.update(prev[i] + offset, 1);
//            while (head < sortedByR.length && sortedByR[head].r == i) {
//                sortedByR[head].ans += sortedByR[head].r - sortedByR[head].l + 1 -
//                        bit.query(sortedByR[head].l + offset, sortedByR[head].r + offset);
//                head++;
//            }
//        }
        Query[] sortedByL = qs.clone();
        Arrays.sort(sortedByL, Comparator.comparingInt(x -> x.l));
        Range2DequeAdapter<Query> dq = new Range2DequeAdapter<>(i -> sortedByL[i], 0, q - 1);
        int offset = 1;
        IntegerBITExt range = new IntegerBITExt(m);
        rects.sort(Comparator.comparingInt(x -> x.l));
        Deque<Rect> rectSortByLDq = new ArrayDeque<>(rects);
        rects.sort(Comparator.comparingInt(x -> x.r));
        Deque<Rect> rectSortByRDq = new ArrayDeque<>(rects);
        for (int i = 0; i < m; i++) {
            while (!rectSortByLDq.isEmpty() && rectSortByLDq.peekFirst().l <= i) {
                Rect head = rectSortByLDq.removeFirst();
                range.update(head.b + offset, head.t + offset, 1);
            }
            while (!rectSortByRDq.isEmpty() && rectSortByRDq.peekFirst().r < i) {
                Rect head = rectSortByRDq.removeFirst();
                range.update(head.b + offset, head.t + offset, -1);
            }
            while (!dq.isEmpty() && dq.peekFirst().l == i) {
                Query head = dq.removeFirst();
                if(range.query(head.r + offset, head.r + offset) == 0){
                    head.ans++;
                }
            }
        }

        for (Query query : qs) {
            out.println(query.ans);
        }
    }


}

class Rect {
    int l;
    int r;
    int b;
    int t;

    public Rect(int l, int r, int b, int t) {
        this.l = l;
        this.r = r;
        this.b = b;
        this.t = t;
    }
}

class Query {
    int l;
    int r;
    int ans;
}