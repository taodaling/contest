package contest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import template.FastInput;
import template.FastOutput;
import template.NumberTheoryTransform;

public class TaskE {

    Modular mod = new Modular(998244353);
    NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 3);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        Set<Integer> left = new LinkedHashSet<>();
        Set<Integer> right = new LinkedHashSet<>();
        left.add(0);
        right.add(0);

        Set<Integer> all = new HashSet<>();
        all.add(0);

        for (int i = 1; i <= 21; i++) {
            int min = (1 << (i - 1));
            int max = (1 << i) - 1;
            Set<Integer> localLeft = new HashSet<>();
            Set<Integer> localRight = new HashSet<>();

            for (int l : left) {
                for (int r : right) {
                    if (l + r + 1 < min || l + r + 1 > max) {
                        continue;
                    }
                    if (level(l) > level(r + 1) || level(l + 1) < level(r)) {
                        continue;
                    }

                    all.add(l + r + 1);
                    if ((l + 1) % 2 == (l + r + 1) % 2) {
                        localLeft.add(l + r + 1);
                    }
                    if ((l + 1) % 2 == 0) {
                        localRight.add(l + r + 1);
                    }
                }
            }

            left.addAll(localLeft);
            right.addAll(localRight);
        }

        out.println(all.contains(n) ? 1 : 0);
    }

    Log2 log2 = new Log2();

    public int level(int x) {
        return log2.ceilLog(x + 1);
    }
}


class Node {
    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();
}
