package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class DSocialCircles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] l = new int[n];
        int[] r = new int[n];
        for(int i = 0; i < n; i++){
            l[i] = in.readInt();
            r[i] = in.readInt();
        }
        Randomized.shuffle(l);
        Randomized.shuffle(r);
        Arrays.sort(l);
        Arrays.sort(r);
        long ans = 0;
        for(int i = 0; i < n; i++){
            ans += Math.max(l[i], r[i]);
        }
        out.println(ans + n);
    }
}
