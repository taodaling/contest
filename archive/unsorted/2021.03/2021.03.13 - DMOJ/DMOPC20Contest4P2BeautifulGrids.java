package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class DMOPC20Contest4P2BeautifulGrids {
    private void flip(TreeSet<Long> set, Long x) {
        if (set.contains(x)) {
            set.remove(x);
        } else {
            set.add(x);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long m = in.rl();
        int k = in.ri();
        TreeSet<Long> row = new TreeSet<>();
        TreeSet<Long> col = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            long x = in.rl();
            long y = in.rl();
            flip(row, x);
            flip(col, y);
        }
        out.println(Math.max(row.size(), col.size()));
        while (!row.isEmpty() || !col.isEmpty()) {
            long x;
            long y;
            if (!row.isEmpty()) {
                x = row.pollFirst();
                y = col.isEmpty() ? 1 : col.pollFirst();
            } else {
                x = 1;
                y = col.pollFirst();
            }
            out.append(x).append(' ').append(y).println();
        }
    }
}
