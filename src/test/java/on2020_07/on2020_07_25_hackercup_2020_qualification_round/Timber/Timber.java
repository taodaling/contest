package on2020_07.on2020_07_25_hackercup_2020_qualification_round.Timber;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.Arrays;

public class Timber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        Tree[] trees = new Tree[n];
        for (int i = 0; i < n; i++) {
            trees[i] = new Tree();
            trees[i].p = in.readInt();
            trees[i].h = in.readInt();
        }
        Arrays.sort(trees, (a, b) -> Integer.compare(a.p, b.p));
        long best = 0;
        LongHashMap map = new LongHashMap(n * 2, true);
        for (int i = 0; i < n; i++) {
            //left side
            for (int k = 1; k >= 0; k--) {
                int lend = (k - 1) * trees[i].h + trees[i].p;
                int rend = k * trees[i].h + trees[i].p;
                long dp = map.getOrDefault(lend, 0) + trees[i].h;
                map.put(rend, Math.max(map.getOrDefault(rend, 0), dp));
                best = Math.max(best, dp);
            }
        }

        out.println(best);
    }
}

class Tree {
    int p;
    int h;
}