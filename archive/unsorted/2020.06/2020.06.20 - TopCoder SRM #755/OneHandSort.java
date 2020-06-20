package contest;

import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class OneHandSort {
    public int[] sortShelf(int[] target) {
        int n = target.length;
        List<Integer> seq = new ArrayList<>(3 * n);
        for (int i = 0; i < n - 1; i++) {
            if (target[i] == i) {
                continue;
            }
            for (int j = i + 1; ; j++) {
                if (target[j] == i) {
                    seq.add(i);
                    seq.add(j);
                    seq.add(n);
                    SequenceUtils.swap(target, i, j);
                    break;
                }
            }
        }
        return seq.stream().mapToInt(Integer::intValue).toArray();
    }
}
