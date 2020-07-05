package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EInversionSwapSort {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        int[] b = a.clone();
        IntegerArrayList list = new IntegerArrayList(n);
        List<int[]> ans = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            list.clear();
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[i]) {
                    list.add(j);
                }
            }
            list.sort((x, y) -> a[x] == a[y] ? Integer.compare(x, y) : Integer.compare(a[x], a[y]));
            list.reverse();
            for (int j = 0; j < list.size(); j++) {
                int t = list.get(j);
                SequenceUtils.swap(b, i, t);
                ans.add(new int[]{i, t});
            }
            //debug.debug("b", Arrays.toString(b));
        }

        for (int i = 1; i < n; i++) {
            if (b[i] < b[i - 1]) {
                throw new RuntimeException();
            }
        }

        out.println(ans.size());
        for (int[] pair : ans) {
            out.append(pair[0] + 1).append(' ').append(pair[1] + 1).println();
        }
    }
}
