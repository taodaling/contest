package on2020_03.on2020_03_02_AtCoder_Regular_Contest_080.F___Prime_Flip1;




import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.MillerRabin;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.Hash;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FPrimeFlip {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            x[i] = in.readInt();
            add(x[i]);
            add(x[i] + 1);
        }
        int[] points = set.stream().mapToInt(Integer::intValue).toArray();
        Randomized.shuffle(points);
        Arrays.sort(points);
        debug.debug("points", points);
        n = points.length;
        KMAlgo km = new KMAlgo(points.length, points.length);
        for (int i = 0; i < n; i++) {
            if (points[i] % 2 != 0) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (points[j] % 2 == 0) {
                    continue;
                }
                int dist = Math.abs(points[i] - points[j]);
                if (mr.mr(dist, 10)) {
                    km.addEdge(i, j, false);
                }
            }
        }

        int cost = 0;
        for (int i = 0; i < n; i++) {
            cost += km.matchLeft(i) ? 1 : 0;
        }

        IntegerList unmatched = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (points[i] % 2 == 0 && km.getLeftMate(i) == -1) {
                unmatched.add(points[i]);
            }
            if (points[i] % 2 == 1 && km.getRightMate(i) == -1) {
                unmatched.add(points[i]);
            }
        }

        unmatched.sort();
        int[] cnts = new int[2];
        for (int i = 0; i < unmatched.size(); i++) {
            cnts[unmatched.get(i) % 2]++;
        }
        cost += (cnts[0] / 2 + cnts[1] / 2) * 2;
        cost += cnts[0] % 2 * 3;

        out.println(cost);
    }

    Set<Integer> set = new HashSet<>(200);

    public void add(int x) {
        if (set.contains(x)) {
            set.remove(x);
        } else {
            set.add(x);
        }
    }

    MillerRabin mr = new MillerRabin();
}
