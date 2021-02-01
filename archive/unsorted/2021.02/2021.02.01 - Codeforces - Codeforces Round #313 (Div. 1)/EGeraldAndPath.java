package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongIntervalMap;
import template.problem.StickFallProblem;
import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import javax.sound.midi.Sequence;
import java.util.Arrays;
import java.util.Comparator;

public class EGeraldAndPath {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] x = new long[n];
        long[] l = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.ri();
            l[i] = in.ri();
        }
        Pair<int[], Long> res = StickFallProblem.solve(x, l);
        long ans = res.b;
        debug.debug("res.a", res.a);
        debug.debug("res.b", res.b);
        out.println(ans);
        LongIntervalMap map = new LongIntervalMap();
        for (int i = 0; i < n; i++) {
            if (res.a[i] == -1) {
                map.add(x[i] - l[i], x[i]);
            } else {
                map.add(x[i], x[i] + l[i]);
            }
        }
        if (map.total() != res.b) {
            throw new RuntimeException();
        }
    }
}