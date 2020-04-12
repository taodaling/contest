package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class CCircleOfMonsters {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        long[] b = new long[n];
        long[] save = new long[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
            b[i] = in.readLong();
            sum += a[i];
        }
        for(int i = 0; i < n; i++){
            int next = (i + 1) % n;
            save[i] = Math.min(b[i], a[next]);
        }
        Randomized.shuffle(save);
        Arrays.sort(save);
        debug.debug("save", save);
        for(int i = 1; i < n; i++){
            sum -= save[i];
        }
        out.println(sum);
    }
}
