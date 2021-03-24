package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AKnapsack {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long w = in.readLong();
        long[] vs = new long[n];
        in.populate(vs);
        IntegerArrayList list = new IntegerArrayList(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> -Long.compare(vs[i], vs[j]), 0, n);
        long sum = 0;
        for (int i : indices) {
            if (vs[i] + sum <= w) {
                list.add(i);
                sum += vs[i];
            }
        }
        if (sum * 2 < w) {
            out.println(-1);
            return;
        }
        out.println(list.size());
        for(int i : list.toArray()){
            out.append(i + 1).append(' ');
        }
        out.println();
    }
}
