package on2020_12.on2020_12_07_Codeforces___Codeforces_Global_Round_12.F__The_Struggling_Contestant;



import com.fasterxml.jackson.databind.SequenceWriter;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class FTheStrugglingContestant {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        EndPoint[] eps = new EndPoint[n + 1];
        for (int i = 0; i <= n; i++) {
            eps[i] = new EndPoint();
            eps[i].v = i;
        }
        for (int i = 1; i < n; i++) {
            if (a[i - 1] == a[i]) {
                eps[a[i]].cnt++;
                eps[a[i - 1]].cnt++;
            }
        }
        eps[a[0]].cnt++;
        eps[a[n - 1]].cnt++;
        Arrays.sort(eps, (x, y) -> Integer.compare(x.cnt, y.cnt));
        SequenceUtils.reverse(eps);

        int sum = 0;
        for (EndPoint ep : eps) {
            sum += ep.cnt;
        }
        int max = eps[0].cnt;
        int maxV = eps[0].v;
        debug.debug("sum", sum);
        debug.debug("max", max);
        debug.debug("maxV", maxV);
        for (int i = 1; i < n; i++) {
            if (max - 2 > sum - max && a[i - 1] != a[i] && a[i - 1] != maxV &&
                    a[i] != maxV) {
                sum += 2;
            }
        }
        if (max - 2 > sum - max) {
            out.println(-1);
        } else {
            out.println((sum - 2) / 2);
        }
    }
}

class EndPoint {
    int v;
    int cnt;
}
