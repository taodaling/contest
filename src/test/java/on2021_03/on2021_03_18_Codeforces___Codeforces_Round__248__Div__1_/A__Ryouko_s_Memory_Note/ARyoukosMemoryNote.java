package on2021_03.on2021_03_18_Codeforces___Codeforces_Round__248__Div__1_.A__Ryouko_s_Memory_Note;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class ARyoukosMemoryNote {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();

        int[] a = in.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        IntegerArrayList list = new IntegerArrayList();
        long sum = 0;
        for (int i = 1; i < n; i++) {
            sum += Math.abs(a[i] - a[i - 1]);
        }

        long best = sum;
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i;
            int x = a[indices[i]];
            while ((r + 1) < n && a[indices[r + 1]] == x) {
                r++;
            }
            i = r;
            list.clear();
            for (int j = l; j <= r; j++) {
                int index = indices[j];
                if (index > 0 && a[index - 1] != x) {
                    list.add(a[index - 1]);
                }
                if (index + 1 < n && a[index + 1] != x) {
                    list.add(a[index + 1]);
                }
            }
            list.sort();
            long prevSum = 0;
            long nextSum = 0;
            int y = list.isEmpty() ? -1 : list.get(list.size() / 2);
            for (int j = 0; j < list.size(); j++) {
                prevSum += Math.abs(list.get(j) - x);
                nextSum += Math.abs(list.get(j) - y);
            }
            best = Math.min(best, sum - prevSum + nextSum);
        }
        out.println(best);
    }
}
